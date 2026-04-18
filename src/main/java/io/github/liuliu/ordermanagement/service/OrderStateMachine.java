package io.github.liuliu.ordermanagement.service;

import io.github.liuliu.ordermanagement.domain.enumtype.OrderState;
import io.github.liuliu.ordermanagement.exception.BusinessRuleException;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * Manages the lifecycle and state transitions of an Order.
 * 
 * Transition Rules:
 * - DRAFT -> SUBMITTED (User submits)
 * - DRAFT -> CANCELLED (User/System cancels)
 * - SUBMITTED -> APPROVED (Internal approval)
 * - SUBMITTED -> CANCELLED (User/System cancels)
 * - APPROVED / CANCELLED are terminal states in v1.
 */
@Component
public class OrderStateMachine {

    private static final Map<OrderState, Set<OrderState>> VALID_TRANSITIONS = Map.of(
            OrderState.DRAFT, EnumSet.of(OrderState.SUBMITTED, OrderState.CANCELLED),
            OrderState.SUBMITTED, EnumSet.of(OrderState.APPROVED, OrderState.CANCELLED),
            OrderState.APPROVED, EnumSet.noneOf(OrderState.class),
            OrderState.CANCELLED, EnumSet.noneOf(OrderState.class)
    );

    /**
     * Validates if a transition from current to target state is allowed.
     *
     * @param current The current state of the order.
     * @param target  The requested target state.
     * @throws BusinessRuleException if the transition is illegal.
     */
    public void validateTransition(OrderState current, OrderState target) {
        if (current == target) {
            return;
        }

        Set<OrderState> allowed = VALID_TRANSITIONS.getOrDefault(current, EnumSet.noneOf(OrderState.class));
        if (!allowed.contains(target)) {
            throw new BusinessRuleException(
                    String.format("Invalid state transition: %s -> %s", current, target)
            );
        }
    }

    /**
     * Checks if the order is in a state that allows modification of its details (e.g., amount).
     * Based on PLAN.md: DRAFT and SUBMITTED allow orderAmount modification.
     *
     * @param state The current state.
     * @return true if editable.
     */
    public boolean isEditable(OrderState state) {
        return state == OrderState.DRAFT || state == OrderState.SUBMITTED;
    }

    /**
     * Checks if the order is in a terminal state.
     *
     * @param state The current state.
     * @return true if APPROVED or CANCELLED.
     */
    public boolean isTerminal(OrderState state) {
        return state == OrderState.APPROVED || state == OrderState.CANCELLED;
    }
}
