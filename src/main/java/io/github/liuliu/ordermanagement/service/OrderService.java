package io.github.liuliu.ordermanagement.service;

import io.github.liuliu.ordermanagement.assembler.OrderAssembler;
import io.github.liuliu.ordermanagement.calculator.CalculatorRegistry;
import io.github.liuliu.ordermanagement.domain.dto.CreateOrderCommandDto;
import io.github.liuliu.ordermanagement.domain.dto.CreateOrderResultDto;
import io.github.liuliu.ordermanagement.domain.dto.GetOrdersByUserQueryDto;
import io.github.liuliu.ordermanagement.domain.dto.OrderDto;
import io.github.liuliu.ordermanagement.domain.dto.OrderPagedResult;
import io.github.liuliu.ordermanagement.domain.dto.PagedOrderDto;
import io.github.liuliu.ordermanagement.domain.dto.PatchOrderCommandDto;
import io.github.liuliu.ordermanagement.domain.entity.OrderEntity;
import io.github.liuliu.ordermanagement.domain.entity.ProductCategoryEntity;
import io.github.liuliu.ordermanagement.domain.entity.ProductEntity;
import io.github.liuliu.ordermanagement.domain.entity.UserEntity;
import io.github.liuliu.ordermanagement.domain.enumtype.OrderState;
import io.github.liuliu.ordermanagement.domain.enumtype.OrderUpdateCheckResult;
import io.github.liuliu.ordermanagement.exception.BusinessRuleException;
import io.github.liuliu.ordermanagement.exception.ResourceNotFoundException;
import io.github.liuliu.ordermanagement.lockkey.AdvisoryLockKeys;
import io.github.liuliu.ordermanagement.storage.Storage;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final Storage storage;
    private final OrderStateMachine stateMachine;
    private final CalculatorRegistry calculatorRegistry;

    @Transactional
    public CreateOrderResultDto createOrder(CreateOrderCommandDto request) {
        // TODO: idempotency key.
        // The read path below can later be folded into a single write-oriented SQL flow if needed.
        @NonNull final UUID userId = request.getUserId();
        storage.findUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        // Re-fetch and lock product/category rows inside the same transaction
        // so totalCost calculation does not observe mutable master data that can change mid-flow.
        ProductEntity product = storage.findProductAndCategoryForUpdate(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + request.getProductId()));

        @NonNull BigDecimal expectedTaxRate = request.getExpectedTaxRate();
        if (!expectedTaxRate.equals(product.getTaxRate())) {
            throw new BusinessRuleException("Expected unit price does not match current product price");
        }
        @NonNull BigDecimal expectedUnitPrice = request.getExpectedUnitPrice();
        if (!expectedUnitPrice.equals(product.getUnitPrice())) {
            throw new BusinessRuleException("Expected tax rate does not match current product category tax rate");
        }

        BigDecimal totalCost = calculatorRegistry.calculate(
                product.getCalculationType(),
                request.getOrderAmount(),
                expectedUnitPrice,
                expectedTaxRate
        );

        // Snapshot values used for order creation should come from the same consistency boundary
        // as the insert, or be protected by a write-oriented statement/locking strategy.
        OrderEntity order = OrderEntity.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .productId(product.getId())
                .orderAmount(request.getOrderAmount())
                .unitPriceSnapshot(expectedUnitPrice)
                .taxRateSnapshot(expectedTaxRate)
                .totalCost(totalCost)
                .status(OrderState.DRAFT)
                .build();

        return storage.saveOrder(order)
                .map(OrderAssembler::toCreateOrderResultDto)
                .orElseThrow(() -> new BusinessRuleException("Unhandled error when create order"));
    }

    @Transactional
    public OrderDto patchOrder(PatchOrderCommandDto request) {
        final UUID orderId = request.getOrderId();

        // Serialize the whole patch flow for the same order within the current transaction.
        // Storage implementation should back this with PostgreSQL pg_advisory_xact_lock(...).
        storage.acquireTransactionLock(AdvisoryLockKeys.ORDER_NAMESPACE, AdvisoryLockKeys.forOrder(orderId));

        final OrderEntity order = storage.findOrderById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));

        // Early failure. The actual update SQL should include state/version predicates so stale writers fail cleanly.
        if (!stateMachine.isEditable(order.getStatus())) {
            throw new BusinessRuleException("Order in " + order.getStatus() + " state is not editable.");
        }

        final OrderEntity.OrderEntityBuilder updateOrderBuilder = order.toBuilder()
                .taxRateSnapshot(request.getExpectedTaxRate())
                .unitPriceSnapshot(request.getExpectedUnitPrice());
        Integer orderAmount = request.getOrderAmount();
        if (orderAmount != null) {
            updateOrderBuilder.orderAmount(orderAmount);

            // Re-fetch and lock product/category rows inside the same transaction
            // so totalCost calculation does not observe mutable master data that can change mid-flow.
            ProductEntity product = storage.findProductAndCategoryForUpdate(order.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + order.getProductId()));

            @NonNull BigDecimal expectedTaxRate = request.getExpectedTaxRate();
            if (!expectedTaxRate.equals(product.getTaxRate())) {
                throw new BusinessRuleException("Expected unit price does not match current product price");
            }
            @NonNull BigDecimal expectedUnitPrice = request.getExpectedUnitPrice();
            if (!expectedUnitPrice.equals(product.getUnitPrice())) {
                throw new BusinessRuleException("Expected tax rate does not match current product category tax rate");
            }

            BigDecimal totalCost = calculatorRegistry.calculate(
                    product.getCalculationType(),
                    orderAmount,
                    expectedUnitPrice,
                    expectedTaxRate
            );
            updateOrderBuilder.totalCost(totalCost);
        }

        OrderUpdateCheckResult checkResult = storage.updateOrder(updateOrderBuilder.build());
        if (checkResult.equals(OrderUpdateCheckResult.OK)) {
            return storage.findOrderById(orderId).map(OrderAssembler::toOrderDto)
                    .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));
        }

        throw new BusinessRuleException(checkResult.getDescription());
    }

    public void deleteOrder(UUID orderId) {
        storage.softDeleteOrder(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));
    }

    public PagedOrderDto getOrdersByUserId(GetOrdersByUserQueryDto query) {
        storage.findUserById(query.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + query.getUserId()));

        int offset = (query.getPage() - 1) * query.getSize();
        OrderPagedResult pagedResult = storage.findOrdersByUserIdPaged(query.getUserId(), offset, query.getSize());

        return OrderAssembler.toPagedOrderDto(pagedResult, query.getPage(), query.getSize());
    }
}
