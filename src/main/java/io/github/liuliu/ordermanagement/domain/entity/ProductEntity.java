package io.github.liuliu.ordermanagement.domain.entity;

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
@Alias("ProductEntity")
public class ProductEntity {
    private UUID id;
    private UUID productCategoryId;
    private String productName;
    private BigDecimal unitPrice; // decimal(19,4)
    private Boolean isDeleted;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
