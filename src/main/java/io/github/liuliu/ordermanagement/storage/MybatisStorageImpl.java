package io.github.liuliu.ordermanagement.storage;

import io.github.liuliu.ordermanagement.domain.dto.OrderPagedResult;
import io.github.liuliu.ordermanagement.domain.entity.OrderEntity;
import io.github.liuliu.ordermanagement.domain.entity.ProductEntity;
import io.github.liuliu.ordermanagement.domain.entity.UserEntity;
import io.github.liuliu.ordermanagement.domain.enumtype.OrderUpdateCheckResult;
import io.github.liuliu.ordermanagement.mapper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class MybatisStorageImpl implements Storage {

    private final UserMapper userMapper;
    private final OrderMapper orderMapper;
    private final OrderManagementMapper orderManagementMapper;
    private final CommonMapper commonMapper;
    private final ProductManagementMapper productManagementMapper;

    @Override
    public Optional<UserEntity> findUserById(UUID id) {
        return userMapper.findById(id);
    }

    @Override
    public void deleteUserCompletely(UUID userId) {
        orderManagementMapper.deleteUserAndRelatedOrders(userId);
    }

    @Override
    public Optional<ProductEntity> findProductAndCategory(UUID id) {
        return productManagementMapper.findProductAndCategory(id);
    }

    @Override
    public Optional<ProductEntity> findProductAndCategoryForUpdate(UUID id) {
        return productManagementMapper.findProductAndCategoryForUpdate(id);
    }

    @Override
    public Optional<OrderEntity> saveOrder(OrderEntity order) {
        return orderMapper.insertAndReturn(order);
    }

    @Override
    public Optional<OrderEntity> findOrderById(UUID id) {
        return orderMapper.findById(id);
    }

    @Override
    public OrderPagedResult findOrdersByUserIdPaged(UUID userId, int offset, int limit) {
        // TODO: merge findByUserIdPaged() and countByUserId()
        return OrderPagedResult.builder()
                .items(orderMapper.findByUserIdPaged(userId, offset, limit))
                .totalItems(orderMapper.countByUserId(userId))
                .build();
    }

    @Override
    public OrderUpdateCheckResult updateOrder(OrderEntity order) {
        return orderMapper.updateAndReturnCheckResult(order);
    }

    @Override
    public void acquireTransactionLock(int namespace, int resourceKey) {
        commonMapper.acquireTransactionLock(namespace, resourceKey);
    }

    @Override
    public Optional<OrderEntity> softDeleteOrder(UUID id) {
        return orderMapper.softDeleteAndReturn(id);
    }
}
