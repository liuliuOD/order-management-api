package io.github.liuliu.ordermanagement.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Standardized error codes for machine-readable client logic.
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    RESOURCE_NOT_FOUND("ERR_RESOURCE_NOT_FOUND"),
    VALIDATION_FAILED("ERR_VALIDATION_FAILED"),
    BUSINESS_RULE_VIOLATION("ERR_BUSINESS_RULE_VIOLATION"),
    CONFLICT("ERR_CONFLICT"),
    INTERNAL_SERVER_ERROR("ERR_INTERNAL_SERVER_ERROR");

    private final String value;
}
