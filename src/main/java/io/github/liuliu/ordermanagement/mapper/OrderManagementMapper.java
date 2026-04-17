package io.github.liuliu.ordermanagement.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.UUID;

/**
 * Domain-specific Mapper for complex business processes.
 * Handles atomic, cross-table operations such as user-cascading deletions.
 */
@Mapper
public interface OrderManagementMapper {

    /**
     * Executes an atomic soft-delete of a user and all their associated orders.
     * Implemented using a Writable CTE for maximum performance and atomicity.
     */
    void deleteUserAndRelatedOrders(@Param("userId") UUID userId);
}
