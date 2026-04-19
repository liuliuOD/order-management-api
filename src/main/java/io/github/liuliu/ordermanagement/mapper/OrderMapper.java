package io.github.liuliu.ordermanagement.mapper;

import io.github.liuliu.ordermanagement.domain.dto.OrderPagedResult;
import io.github.liuliu.ordermanagement.domain.entity.OrderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Mapper
public interface OrderMapper {

    Optional<OrderEntity> insertAndReturn(OrderEntity order);

    Optional<OrderEntity> findById(@Param("id") UUID id);

    /**
     * Retrieves paged orders for a user.
     */
    List<OrderEntity> findByUserIdPaged(
            @Param("userId") UUID userId,
            @Param("offset") int offset,
            @Param("limit") int limit
    );

    /**
     * Counts active orders for a user.
     */
    long countByUserId(@Param("userId") UUID userId);

    Optional<OrderEntity> update(OrderEntity order);

    /**
     * Soft deletes an order by ID.
     */
    Optional<OrderEntity> softDeleteAndReturn(@Param("id") UUID id);
}
