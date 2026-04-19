package io.github.liuliu.ordermanagement.service;

import io.github.liuliu.ordermanagement.domain.dto.GetProductByIdQueryDto;
import io.github.liuliu.ordermanagement.domain.dto.ProductDto;
import io.github.liuliu.ordermanagement.domain.entity.ProductCategoryEntity;
import io.github.liuliu.ordermanagement.domain.entity.ProductEntity;
import io.github.liuliu.ordermanagement.exception.ResourceNotFoundException;
import io.github.liuliu.ordermanagement.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final Storage storage;

    public ProductDto getProductById(GetProductByIdQueryDto query) {
        // TBD: merge the query of find product & find category
        ProductEntity productEntity = storage.findProductById(query.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + query.getProductId()));

        ProductCategoryEntity categoryEntity = storage.findCategoryById(productEntity.getProductCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + productEntity.getProductCategoryId()));

        // TBD: move to converter
        return ProductDto.builder()
                .id(productEntity.getId())
                .productName(productEntity.getProductName())
                .productCategoryId(productEntity.getProductCategoryId())
                .unitPrice(productEntity.getUnitPrice())
                .taxRate(categoryEntity.getTaxRate())
                .createdAt(productEntity.getCreatedAt())
                .updatedAt(productEntity.getUpdatedAt())
                .build();
    }
}
