package io.github.liuliu.ordermanagement.domain.entity;

import io.github.liuliu.ordermanagement.domain.enumtype.CalculationType;
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
public class ProductCategoryEntity {
    private UUID categoryId;
    private String categoryName;
    private BigDecimal taxRate; // decimal(5,4)
    private CalculationType calculationType;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
