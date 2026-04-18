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
public class ProductDto {
    private UUID id;
    private String productName;
    private UUID productCategoryId;
    private BigDecimal unitPrice;
    private BigDecimal taxRate;
}
