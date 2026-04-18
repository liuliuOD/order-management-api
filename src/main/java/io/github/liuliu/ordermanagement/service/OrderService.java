package io.github.liuliu.ordermanagement.service;

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
import io.github.liuliu.ordermanagement.exception.BusinessRuleException;
import io.github.liuliu.ordermanagement.exception.ResourceNotFoundException;
import io.github.liuliu.ordermanagement.storage.Storage;
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
        UserEntity user = storage.findUserById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.getUserId()));

        ProductEntity product = storage.findProductForOrder(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + request.getProductId()));

        ProductCategoryEntity category = storage.findCategoryById(product.getProductCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + product.getProductCategoryId()));

        BigDecimal totalCost = calculatorRegistry.calculate(
                category.getCalculationType(),
                request.getOrderAmount(),
                product.getUnitPrice(),
                category.getTaxRate()
        );

        OrderEntity order = OrderEntity.builder()
                .id(UUID.randomUUID())
                .userId(user.getId())
                .productId(product.getId())
                .orderAmount(request.getOrderAmount())
                .unitPriceSnapshot(product.getUnitPrice())
                .taxRateSnapshot(category.getTaxRate())
                .totalCost(totalCost)
                .status(OrderState.DRAFT)
                .build();

        storage.saveOrder(order);
        return CreateOrderResultDto.builder()
                .id(order.getId())
                .build();
    }

    @Transactional
    public OrderDto patchOrder(PatchOrderCommandDto request) {
        OrderEntity order = storage.findOrderById(request.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + request.getOrderId()));

        if (!stateMachine.isEditable(order.getStatus())) {
            throw new BusinessRuleException("Order in " + order.getStatus() + " state is not editable.");
        }

        if (request.getOrderAmount() != null) {
            order.setOrderAmount(request.getOrderAmount());

            // Re-fetch category to resolve calculation strategy while keeping snapshot values for pricing.
            ProductEntity product = storage.findProductById(order.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + order.getProductId()));
            ProductCategoryEntity category = storage.findCategoryById(product.getProductCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + product.getProductCategoryId()));

            BigDecimal totalCost = calculatorRegistry.calculate(
                    category.getCalculationType(),
                    order.getOrderAmount(),
                    order.getUnitPriceSnapshot(),
                    order.getTaxRateSnapshot()
            );
            order.setTotalCost(totalCost);
        }

        storage.updateOrder(order);
        return toOrderDto(order);
    }

    @Transactional
    public void deleteOrder(UUID orderId) {
        storage.findOrderById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));
        storage.softDeleteOrder(orderId);
    }

    public OrderDto getOrderById(UUID orderId) {
        return storage.findOrderById(orderId)
                .map(this::toOrderDto)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));
    }

    public PagedOrderDto getOrdersByUserId(GetOrdersByUserQueryDto query) {
        storage.findUserById(query.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + query.getUserId()));

        int offset = (query.getPage() - 1) * query.getSize();
        OrderPagedResult pagedResult = storage.findOrdersByUserIdPaged(query.getUserId(), offset, query.getSize());
        List<OrderDto> items = pagedResult.getItems().stream()
                .map(this::toOrderDto)
                .collect(Collectors.toList());

        return PagedOrderDto.builder()
                .items(items)
                .page(query.getPage())
                .size(query.getSize())
                .totalItems(pagedResult.getTotalItems())
                .build();
    }

    private OrderDto toOrderDto(OrderEntity order) {
        return OrderDto.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .productId(order.getProductId())
                .orderAmount(order.getOrderAmount())
                .unitPriceSnapshot(order.getUnitPriceSnapshot())
                .taxRateSnapshot(order.getTaxRateSnapshot())
                .totalCost(order.getTotalCost())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }
}
