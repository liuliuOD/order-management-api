package io.github.liuliu.ordermanagement.converter;

import io.github.liuliu.model.CreateOrderRequest;
import io.github.liuliu.model.CreateOrderResponse;
import io.github.liuliu.model.Order;
import io.github.liuliu.model.OrderStatus;
import io.github.liuliu.model.PagedOrderResponse;
import io.github.liuliu.model.PaginationInfo;
import io.github.liuliu.model.PatchOrderRequest;
import io.github.liuliu.ordermanagement.domain.dto.CreateOrderCommandDto;
import io.github.liuliu.ordermanagement.domain.dto.CreateOrderResultDto;
import io.github.liuliu.ordermanagement.domain.dto.GetOrdersByUserQueryDto;
import io.github.liuliu.ordermanagement.domain.dto.OrderDto;
import io.github.liuliu.ordermanagement.domain.dto.PagedOrderDto;
import io.github.liuliu.ordermanagement.domain.dto.PatchOrderCommandDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OrderConverter {

    public CreateOrderCommandDto toCreateOrderCommandDto(CreateOrderRequest request) {
        if (request == null) {
            return null;
        }
        return CreateOrderCommandDto.builder()
                .userId(request.getUserId())
                .productId(request.getProductId())
                .orderAmount(request.getOrderAmount())
                .build();
    }

    public PatchOrderCommandDto toPatchOrderCommandDto(UUID orderId, PatchOrderRequest request) {
        if (request == null) {
            return null;
        }
        return PatchOrderCommandDto.builder()
                .orderId(orderId)
                .orderAmount(request.getOrderAmount())
                .build();
    }

    public GetOrdersByUserQueryDto toGetOrdersByUserQueryDto(UUID userId, Integer page, Integer size) {
        return GetOrdersByUserQueryDto.builder()
                .userId(userId)
                .page(page)
                .size(size)
                .build();
    }

    public CreateOrderResponse toCreateOrderResponse(CreateOrderResultDto result) {
        if (result == null) {
            return null;
        }
        CreateOrderResponse response = new CreateOrderResponse();
        response.setId(result.getId());
        return response;
    }

    public Order toOrderResponse(OrderDto dto) {
        if (dto == null) {
            return null;
        }
        Order response = new Order();
        response.setId(dto.getId());
        response.setUserId(dto.getUserId());
        response.setProductId(dto.getProductId());
        response.setOrderAmount(dto.getOrderAmount());
        response.setUnitPriceSnapshot(dto.getUnitPriceSnapshot());
        response.setTaxRateSnapshot(dto.getTaxRateSnapshot());
        response.setTotalCost(dto.getTotalCost());
        if (dto.getStatus() != null) {
            response.setStatus(OrderStatus.fromValue(dto.getStatus().name()));
        }
        response.setCreatedAt(dto.getCreatedAt());
        response.setUpdatedAt(dto.getUpdatedAt());
        return response;
    }

    public PagedOrderResponse toPagedOrderResponse(PagedOrderDto dto) {
        PagedOrderResponse response = new PagedOrderResponse();
        List<Order> data = dto.getItems().stream()
                .map(this::toOrderResponse)
                .collect(Collectors.toList());
        response.setData(data);

        PaginationInfo pagination = new PaginationInfo();
        pagination.setPage(dto.getPage());
        pagination.setSize(dto.getSize());
        pagination.setTotalItems(dto.getTotalItems());
        pagination.setTotalPages((int) Math.ceil((double) dto.getTotalItems() / dto.getSize()));
        response.setPagination(pagination);

        return response;
    }
}
