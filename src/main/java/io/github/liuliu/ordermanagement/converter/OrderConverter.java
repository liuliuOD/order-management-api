package io.github.liuliu.ordermanagement.converter;

import io.github.liuliu.model.Order;
import io.github.liuliu.model.CreateOrderResponse;
import io.github.liuliu.ordermanagement.domain.entity.OrderEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrderConverter {

    public Order toDto(OrderEntity entity) {
        if (entity == null) {
            return null;
        }
        Order dto = new Order();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUserId());
        dto.setProductId(entity.getId());
        dto.setOrderAmount(entity.getOrderAmount());
        dto.setUnitPriceSnapshot(entity.getUnitPriceSnapshot());
        dto.setTaxRateSnapshot(entity.getTaxRateSnapshot());
        dto.setTotalCost(entity.getTotalCost());
        if (entity.getStatus() != null) {
            dto.setStatus(io.github.liuliu.model.OrderStatus.fromValue(entity.getStatus().name()));
        }
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public CreateOrderResponse toCreateResponse(OrderEntity entity) {
        if (entity == null) {
            return null;
        }
        CreateOrderResponse response = new CreateOrderResponse();
        response.setId(entity.getId());
        return response;
    }
}
