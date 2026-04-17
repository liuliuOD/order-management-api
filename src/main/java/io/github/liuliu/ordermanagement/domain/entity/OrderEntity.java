package io.github.liuliu.ordermanagement.domain.entity;

import io.github.liuliu.ordermanagement.domain.enumtype.OrderState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {
    private UUID id;
    private UUID userId;
    private UUID productId;
    private Integer orderAmount;
    private BigDecimal unitPriceSnapshot; // decimal(19,4)
    private BigDecimal taxRateSnapshot;   // decimal(5,4)
    private BigDecimal totalCost;         // decimal(19,4)
    private OrderState status;
    private Boolean isDeleted;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private OffsetDateTime deletedAt;
}
