package io.github.liuliu.ordermanagement.storage;

import io.github.liuliu.ordermanagement.domain.dto.OrderPagedResult;
import io.github.liuliu.ordermanagement.domain.entity.OrderEntity;
import io.github.liuliu.ordermanagement.domain.entity.ProductCategoryEntity;
import io.github.liuliu.ordermanagement.domain.entity.ProductEntity;
import io.github.liuliu.ordermanagement.domain.entity.UserEntity;
import io.github.liuliu.ordermanagement.domain.enumtype.OrderUpdateCheckResult;

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
    Optional<ProductEntity> findProductAndCategory(UUID id);
    Optional<ProductEntity> findProductAndCategoryForUpdate(UUID id);

    // Order Operations
    Optional<OrderEntity> saveOrder(OrderEntity order);
    Optional<OrderEntity> findOrderById(UUID id);
    OrderPagedResult findOrdersByUserIdPaged(UUID userId, int offset, int limit);
    OrderUpdateCheckResult updateOrder(OrderEntity order);
    Optional<OrderEntity> softDeleteOrder(UUID id);

    /**
     * Acquire a transaction-scoped advisory lock using caller-provided lock keys.
     *
     * <p>The service layer is responsible for deciding lock identity and namespace,
     * while storage only executes the transaction-bound advisory lock call.</p>
     *
     * <p>Implementation is expected to use a transaction-bound advisory lock,
     * e.g. PostgreSQL {@code pg_advisory_xact_lock(namespace, resourceKey)}.</p>
     *
     * @param namespace logical lock namespace supplied by the caller
     * @param resourceKey logical resource key supplied by the caller
     */
    void acquireTransactionLock(int namespace, int resourceKey);
}
