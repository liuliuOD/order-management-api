package io.github.liuliu.ordermanagement.domain.enumtype;

import lombok.Getter;
import org.apache.ibatis.type.Alias;

@Getter
@Alias("OrderUpdateCheckResult")
public enum OrderUpdateCheckResult {
    OK("Operation completed successfully"),
    ORDER_NOT_FOUND("Order not found"),
    ORDER_DELETED("Order has been deleted"),
    ORDER_NOT_EDITABLE("Order is not editable in current status"),
    PRODUCT_NOT_AVAILABLE("Product is not available"),
    UNIT_PRICE_MISMATCH("Expected unit price does not match current product price"),
    TAX_RATE_MISMATCH("Expected tax rate does not match current product category tax rate");

    private final String description;

    OrderUpdateCheckResult(String description) {
        this.description = description;
    }
}
