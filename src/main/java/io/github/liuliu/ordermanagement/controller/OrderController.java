package io.github.liuliu.ordermanagement.controller;

import io.github.liuliu.api.OrderApi;
import io.github.liuliu.model.CreateOrderRequest;
import io.github.liuliu.model.CreateOrderResponse;
import io.github.liuliu.model.Order;
import io.github.liuliu.model.PagedOrderResponse;
import io.github.liuliu.model.PatchOrderRequest;
import io.github.liuliu.ordermanagement.converter.OrderConverter;
import io.github.liuliu.ordermanagement.domain.dto.CreateOrderCommandDto;
import io.github.liuliu.ordermanagement.domain.dto.CreateOrderResultDto;
import io.github.liuliu.ordermanagement.domain.dto.GetOrdersByUserQueryDto;
import io.github.liuliu.ordermanagement.domain.dto.OrderDto;
import io.github.liuliu.ordermanagement.domain.dto.PagedOrderDto;
import io.github.liuliu.ordermanagement.domain.dto.PatchOrderCommandDto;
import io.github.liuliu.ordermanagement.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class OrderController implements OrderApi {

    private final OrderService orderService;
    private final OrderConverter orderConverter;

    @Override
    public ResponseEntity<CreateOrderResponse> createOrder(CreateOrderRequest createOrderRequest) {
        CreateOrderCommandDto command = orderConverter.toCreateOrderCommandDto(createOrderRequest);
        CreateOrderResultDto result = orderService.createOrder(command);
        return ResponseEntity.status(201).body(orderConverter.toCreateOrderResponse(result));
    }

    @Override
    public ResponseEntity<Order> patchOrderById(UUID orderId, PatchOrderRequest patchOrderRequest) {
        PatchOrderCommandDto command = orderConverter.toPatchOrderCommandDto(orderId, patchOrderRequest);
        OrderDto order = orderService.patchOrder(command);
        return ResponseEntity.ok(orderConverter.toOrderResponse(order));
    }

    @Override
    public ResponseEntity<Void> deleteOrderById(UUID orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<PagedOrderResponse> getOrdersByUserId(UUID userId, Integer page, Integer size) {
        GetOrdersByUserQueryDto query = orderConverter.toGetOrdersByUserQueryDto(userId, page, size);
        PagedOrderDto result = orderService.getOrdersByUserId(query);
        return ResponseEntity.ok(orderConverter.toPagedOrderResponse(result));
    }
}
