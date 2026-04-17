package io.github.liuliu.ordermanagement.mapper;

import io.github.liuliu.ordermanagement.domain.entity.ProductCategoryEntity;
import io.github.liuliu.ordermanagement.domain.entity.ProductEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;
import java.util.UUID;

@Mapper
public interface ProductMapper {

    Optional<ProductEntity> findById(@Param("id") UUID id);

    Optional<ProductCategoryEntity> findCategoryById(@Param("categoryId") UUID categoryId);

    /**
     * Joins Product and Category to get price and tax in one go.
     */
    Optional<ProductEntity> findProductWithCategory(@Param("id") UUID id);

    void insertProduct(ProductEntity product);

    void insertCategory(ProductCategoryEntity category);
}
