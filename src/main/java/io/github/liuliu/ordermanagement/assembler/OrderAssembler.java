package io.github.liuliu.ordermanagement.assembler;

import io.github.liuliu.ordermanagement.domain.dto.CreateOrderResultDto;
import io.github.liuliu.ordermanagement.domain.dto.OrderDto;
import io.github.liuliu.ordermanagement.domain.dto.OrderPagedResult;
import io.github.liuliu.ordermanagement.domain.dto.PagedOrderDto;
import io.github.liuliu.ordermanagement.domain.entity.OrderEntity;

import java.util.List;
import java.util.stream.Collectors;

public class OrderAssembler {

    public static CreateOrderResultDto toCreateOrderResultDto(OrderEntity orderEntity) {
        return CreateOrderResultDto.builder()
                .id(orderEntity.getId())
                .build();
    }

    public static OrderDto toOrderDto(OrderEntity orderEntity) {
        return OrderDto.builder()
                .id(orderEntity.getId())
                .userId(orderEntity.getUserId())
                .productId(orderEntity.getProductId())
                .orderAmount(orderEntity.getOrderAmount())
                .unitPriceSnapshot(orderEntity.getUnitPriceSnapshot())
                .taxRateSnapshot(orderEntity.getTaxRateSnapshot())
                .totalCost(orderEntity.getTotalCost())
                .status(orderEntity.getStatus())
                .createdAt(orderEntity.getCreatedAt())
                .updatedAt(orderEntity.getUpdatedAt())
                .build();
    }

    public static PagedOrderDto toPagedOrderDto(OrderPagedResult pagedResult, Integer page, Integer size) {
        List<OrderDto> items = pagedResult.getItems().stream()
                .map(OrderAssembler::toOrderDto)
                .collect(Collectors.toList());
        return PagedOrderDto.builder()
                .items(items)
                .page(page)
                .size(size)
                .totalItems(pagedResult.getTotalItems())
                .build();
    }
}
