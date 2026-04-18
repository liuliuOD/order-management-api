package io.github.liuliu.ordermanagement.domain.dto;

import io.github.liuliu.ordermanagement.domain.enumtype.OrderState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private UUID id;
    private UUID userId;
    private UUID productId;
    private Integer orderAmount;
    private BigDecimal unitPriceSnapshot;
    private BigDecimal taxRateSnapshot;
    private BigDecimal totalCost;
    private OrderState status;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
