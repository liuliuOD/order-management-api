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
    UNSUPPORTED_MEDIA_TYPE("ERR_UNSUPPORTED_MEDIA_TYPE"),
    CONFLICT("ERR_CONFLICT"),
    INVALID_REQUEST_BODY("ERR_INVALID_REQUEST_BODY"),
    INTERNAL_SERVER_ERROR("ERR_INTERNAL_SERVER_ERROR");

    private final String value;
}
