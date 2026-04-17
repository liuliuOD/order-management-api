package io.github.liuliu.ordermanagement.domain.dto;

import io.github.liuliu.ordermanagement.domain.entity.OrderEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.util.List;

/**
 * Internal DTO to receive aggregated JSON results from PostgreSQL.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Alias("OrderPagedResult")
public class OrderPagedResult {
    private List<OrderEntity> items;
    private long totalItems;
}
