package io.github.liuliu.ordermanagement.assembler;

import io.github.liuliu.ordermanagement.domain.dto.ProductDto;
import io.github.liuliu.ordermanagement.domain.entity.ProductEntity;

public class ProductAssembler {

    public static ProductDto toProductDto(ProductEntity productEntity) {
        return ProductDto.builder()
                .id(productEntity.getId())
                .productName(productEntity.getProductName())
                .productCategoryId(productEntity.getProductCategoryId())
                .unitPrice(productEntity.getUnitPrice())
                .taxRate(productEntity.getTaxRate())
                .createdAt(productEntity.getCreatedAt())
                .updatedAt(productEntity.getUpdatedAt())
                .build();
    }
}
