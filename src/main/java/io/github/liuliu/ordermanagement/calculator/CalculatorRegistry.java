package io.github.liuliu.ordermanagement.calculator;

import io.github.liuliu.ordermanagement.domain.enumtype.CalculationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Registry and dispatcher for Order calculators.
 */
@Service
@RequiredArgsConstructor
public class CalculatorRegistry {

    private final List<OrderTotalCostCalculator> calculators;
    private final DefaultOrderTotalCostCalculator defaultCalculator;

    public BigDecimal calculate(CalculationType type, Integer orderAmount, BigDecimal unitPriceSnapshot, BigDecimal taxRateSnapshot) {
        return calculators.stream()
                .filter(c -> c.supports(type))
                .findFirst()
                .orElse(defaultCalculator)
                .calculate(orderAmount, unitPriceSnapshot, taxRateSnapshot);
    }
}
