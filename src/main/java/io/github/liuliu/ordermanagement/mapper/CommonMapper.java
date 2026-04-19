package io.github.liuliu.ordermanagement.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.UUID;

/**
 * Domain-specific Mapper for complex business processes.
 * Handles atomic, cross-table operations such as user-cascading deletions.
 */
@Mapper
public interface CommonMapper {

    /**
     * Acquire a PostgreSQL transaction-scoped advisory lock.
     *
     * <p>This method is intentionally generic. The caller provides the lock namespace
     * and logical resource key, while the mapper only issues the database lock call.</p>
     *
     * <p>The corresponding SQL should use {@code pg_advisory_xact_lock(namespace, resourceKey)}
     * so the lock is released automatically when the current transaction ends.</p>
     */
    String acquireTransactionLock(@Param("namespace") int namespace,
                                @Param("resourceKey") int resourceKey);
}
