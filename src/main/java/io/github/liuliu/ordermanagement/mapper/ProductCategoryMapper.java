package io.github.liuliu.ordermanagement.mapper;

import io.github.liuliu.ordermanagement.domain.entity.ProductCategoryEntity;
import io.github.liuliu.ordermanagement.domain.entity.ProductEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;
import java.util.UUID;

@Mapper
public interface ProductCategoryMapper {

    Optional<ProductCategoryEntity> findById(@Param("categoryId") UUID categoryId);

    // only for unittest now
    void insertCategory(ProductCategoryEntity category);
}
