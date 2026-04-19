package io.github.liuliu.ordermanagement.calculator;

import io.github.liuliu.ordermanagement.domain.enumtype.CalculationType;
import io.github.liuliu.ordermanagement.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Registry and dispatcher for Order calculators.
 */
@Service
public class CalculatorRegistry {

    @Autowired
    private final ElectronicOrderTotalCostCalculator electronicOrderTotalCostCalculator;

    private final Map<CalculationType, OrderTotalCostCalculator> calculators;

    public CalculatorRegistry(ElectronicOrderTotalCostCalculator electronicOrderTotalCostCalculator) {
        this.electronicOrderTotalCostCalculator = electronicOrderTotalCostCalculator;
        this.calculators =
                Map.of(CalculationType.ELECTRONICS, this.electronicOrderTotalCostCalculator);
    }

    public BigDecimal calculate(CalculationType type, Integer orderAmount, BigDecimal unitPriceSnapshot, BigDecimal taxRateSnapshot) {
        if (!calculators.containsKey(type)) {
            throw new ResourceNotFoundException("Calculation type not found");
        }
        return calculators.get(type).calculate(orderAmount, unitPriceSnapshot, taxRateSnapshot);
    }
}
