package io.github.liuliu.ordermanagement.service;

import io.github.liuliu.model.Product;
import io.github.liuliu.ordermanagement.converter.ProductConverter;
import io.github.liuliu.ordermanagement.domain.entity.ProductCategoryEntity;
import io.github.liuliu.ordermanagement.domain.entity.ProductEntity;
import io.github.liuliu.ordermanagement.exception.ResourceNotFoundException;
import io.github.liuliu.ordermanagement.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final Storage storage;
    private final ProductConverter productConverter;

    public Product getProductById(UUID id) {
        ProductEntity productEntity = storage.findProductById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        ProductCategoryEntity categoryEntity = storage.findCategoryById(productEntity.getProductCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + productEntity.getProductCategoryId()));

        Product product = productConverter.toDto(productEntity);
        product.setTaxRate(categoryEntity.getTaxRate());
        return product;
    }
}
