package io.github.liuliu.ordermanagement.domain.enumtype;

import lombok.Getter;
import org.apache.ibatis.type.Alias;

@Getter
@Alias("OrderCreateCheckResult")
public enum OrderCreateCheckResult {
    OK("Operation completed successfully"),
    PRODUCT_NOT_AVAILABLE("Product is not available"),
    CATEGORY_NOT_AVAILABLE("Product category is not available"),
    UNIT_PRICE_MISMATCH("Expected unit price does not match current product price"),
    TAX_RATE_MISMATCH("Expected tax rate does not match current product category tax rate");

    private final String description;

    OrderCreateCheckResult(String description) {
        this.description = description;
    }
}
