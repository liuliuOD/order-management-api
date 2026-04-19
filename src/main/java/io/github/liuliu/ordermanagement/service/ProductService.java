package io.github.liuliu.ordermanagement.service;

import io.github.liuliu.ordermanagement.assembler.ProductAssembler;
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
        ProductEntity productEntity = storage.findProductAndCategory(query.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + query.getProductId()));
        return ProductAssembler.toProductDto(productEntity);
    }
}
