package io.github.liuliu.ordermanagement.storage;

import io.github.liuliu.ordermanagement.domain.dto.OrderPagedResult;
import io.github.liuliu.ordermanagement.domain.entity.OrderEntity;
import io.github.liuliu.ordermanagement.domain.entity.ProductCategoryEntity;
import io.github.liuliu.ordermanagement.domain.entity.ProductEntity;
import io.github.liuliu.ordermanagement.domain.entity.UserEntity;
import io.github.liuliu.ordermanagement.exception.ResourceNotFoundException;
import io.github.liuliu.ordermanagement.mapper.OrderManagementMapper;
import io.github.liuliu.ordermanagement.mapper.OrderMapper;
import io.github.liuliu.ordermanagement.mapper.ProductMapper;
import io.github.liuliu.ordermanagement.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class MybatisStorageImpl implements Storage {

    private final UserMapper userMapper;
    private final ProductMapper productMapper;
    private final OrderMapper orderMapper;
    private final OrderManagementMapper orderManagementMapper;

    @Override
    public Optional<UserEntity> findUserById(UUID id) {
        return userMapper.findById(id);
    }

    @Override
    public void deleteUserCompletely(UUID userId) {
        orderManagementMapper.deleteUserAndRelatedOrders(userId);
    }

    @Override
    public Optional<ProductEntity> findProductById(UUID id) {
        return productMapper.findById(id);
    }

    @Override
    public Optional<ProductCategoryEntity> findCategoryById(UUID categoryId) {
        return productMapper.findCategoryById(categoryId);
    }

    @Override
    public Optional<ProductEntity> findProductForOrder(UUID id) {
        return productMapper.findProductWithCategory(id);
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
        // TBD: merge findByUserIdPaged() and countByUserId()
        return OrderPagedResult.builder()
                .items(orderMapper.findByUserIdPaged(userId, offset, limit))
                .totalItems(orderMapper.countByUserId(userId))
                .build();
    }

    @Override
    public Optional<OrderEntity> updateOrder(OrderEntity order) {
        return orderMapper.update(order);
    }

    @Override
    public Optional<OrderEntity> softDeleteOrder(UUID id) {
        return orderMapper.softDeleteAndReturn(id);
    }
}
