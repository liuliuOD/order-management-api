package io.github.liuliu.ordermanagement.calculator;

import io.github.liuliu.ordermanagement.domain.enumtype.CalculationType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Default implementation of order cost calculation.
 */
@Component
public class DefaultOrderTotalCostCalculator implements OrderTotalCostCalculator {

    private static final int MONEY_SCALE = 4;

    @Override
    public BigDecimal calculate(Integer orderAmount, BigDecimal unitPriceSnapshot, BigDecimal taxRateSnapshot) {
        if (orderAmount == null || unitPriceSnapshot == null || taxRateSnapshot == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal amount = BigDecimal.valueOf(orderAmount);
        BigDecimal taxMultiplier = BigDecimal.ONE.add(taxRateSnapshot);

        return amount.multiply(unitPriceSnapshot)
                .multiply(taxMultiplier)
                .setScale(MONEY_SCALE, RoundingMode.HALF_UP);
    }

    @Override
    public boolean supports(CalculationType type) {
        return CalculationType.DEFAULT.equals(type) || type == null;
    }
}
