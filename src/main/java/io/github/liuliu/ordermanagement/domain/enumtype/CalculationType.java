package io.github.liuliu.ordermanagement.domain.enumtype;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Defines the type of calculation strategy to be applied to an order.
 */
@Getter
@RequiredArgsConstructor
public enum CalculationType {
    ELECTRONICS("Custom rule for electronics"),
    FOOD("Custom rule for food/perishables");

    private final String description;
}
