package io.github.liuliu.ordermanagement.storage;

import io.github.liuliu.ordermanagement.domain.dto.OrderPagedResult;
import io.github.liuliu.ordermanagement.domain.entity.OrderEntity;
import io.github.liuliu.ordermanagement.domain.entity.ProductCategoryEntity;
import io.github.liuliu.ordermanagement.domain.entity.ProductEntity;
import io.github.liuliu.ordermanagement.domain.entity.UserEntity;

import java.util.Optional;
import java.util.UUID;

/**
 * Unified Storage interface for all domain data access.
 */
public interface Storage {

    // User Operations
    Optional<UserEntity> findUserById(UUID id);
    void deleteUserCompletely(UUID userId);

    // Product Operations
    Optional<ProductEntity> findProductById(UUID id);
    Optional<ProductCategoryEntity> findCategoryById(UUID categoryId);
    Optional<ProductEntity> findProductForOrder(UUID id);

    // Order Operations
    Optional<OrderEntity> saveOrder(OrderEntity order);
    Optional<OrderEntity> findOrderById(UUID id);
    OrderPagedResult findOrdersByUserIdPaged(UUID userId, int offset, int limit);
    Optional<OrderEntity> updateOrder(OrderEntity order);
    Optional<OrderEntity> softDeleteOrder(UUID id);
}
