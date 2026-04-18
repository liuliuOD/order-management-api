package io.github.liuliu.ordermanagement.converter;

import io.github.liuliu.model.Product;
import io.github.liuliu.ordermanagement.domain.dto.GetProductByIdQueryDto;
import io.github.liuliu.ordermanagement.domain.dto.ProductDto;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProductConverter {

    public GetProductByIdQueryDto toGetProductByIdQueryDto(UUID productId) {
        return GetProductByIdQueryDto.builder()
                .productId(productId)
                .build();
    }

    public Product toProductResponse(ProductDto dto) {
        if (dto == null) {
            return null;
        }
        Product response = new Product();
        response.setId(dto.getId());
        response.setProductName(dto.getProductName());
        response.setProductCategoryId(dto.getProductCategoryId());
        response.setUnitPrice(dto.getUnitPrice());
        response.setTaxRate(dto.getTaxRate());
        return response;
    }
}
