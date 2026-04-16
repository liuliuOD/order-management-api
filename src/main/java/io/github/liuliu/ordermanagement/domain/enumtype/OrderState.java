package io.github.liuliu.ordermanagement.domain.enumtype;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Represents the lifecycle states of an Order aggregate.
 */
@Getter
@RequiredArgsConstructor
public enum OrderState {
    DRAFT("Initial state, fully editable"),
    SUBMITTED("Locked for review, limited editing"),
    APPROVED("Finalized, no further changes allowed"),
    CANCELLED("Terminal state for aborted orders");

    private final String description;
}
