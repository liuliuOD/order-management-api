package io.github.liuliu.ordermanagement.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatchOrderCommandDto {
    private UUID orderId;
    private Integer orderAmount;
    private BigDecimal expectedTaxRate;
    private BigDecimal expectedUnitPrice;
}
