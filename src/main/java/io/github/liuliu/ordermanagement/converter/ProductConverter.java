package io.github.liuliu.ordermanagement.converter;

import io.github.liuliu.model.Product;
import io.github.liuliu.ordermanagement.domain.entity.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductConverter {

    public Product toDto(ProductEntity entity) {
        if (entity == null) {
            return null;
        }
        Product dto = new Product();
        dto.setId(entity.getId());
        dto.setProductName(entity.getProductName());
        dto.setProductCategoryId(entity.getProductCategoryId());
        dto.setUnitPrice(entity.getUnitPrice());
        // taxRate will be populated by the service layer or a joint entity in future
        return dto;
    }
}
