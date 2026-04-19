package io.github.liuliu.ordermanagement.mapper;

import io.github.liuliu.ordermanagement.domain.entity.ProductCategoryEntity;
import io.github.liuliu.ordermanagement.domain.entity.ProductEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;
import java.util.UUID;

@Mapper
public interface ProductManagementMapper {

    /**
     * Joins Product and Category to get price and tax in one go.
     */
    Optional<ProductEntity> findProductAndCategory(@Param("id") UUID id);

    /**
     * Joins Product and Category to get price and tax in one go with write lock.
     */
    Optional<ProductEntity> findProductAndCategoryForUpdate(@Param("id") UUID id);
}
