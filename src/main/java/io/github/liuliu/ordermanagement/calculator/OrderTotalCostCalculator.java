package io.github.liuliu.ordermanagement.calculator;

import io.github.liuliu.ordermanagement.domain.enumtype.CalculationType;

import java.math.BigDecimal;

/**
 * Strategy interface for calculating the total cost of an order.
 * This allows for different calculation rules based on product categories or business logic.
 */
public interface OrderTotalCostCalculator {

    /**
     * Calculates the total cost based on amount, unit price, and tax rate.
     *
     * @param orderAmount       The quantity of items.
     * @param unitPriceSnapshot The price per unit locked at order creation.
     * @param taxRateSnapshot   The tax rate locked at order creation (e.g., 0.05).
     * @return The calculated total cost.
     */
    BigDecimal calculate(Integer orderAmount, BigDecimal unitPriceSnapshot, BigDecimal taxRateSnapshot);

    /**
     * Determines if this calculator supports a specific calculation type.
     *
     * @param type The type of calculation strategy.
     * @return true if supported.
     */
    boolean supports(CalculationType type);
}
