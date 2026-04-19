package io.github.liuliu.ordermanagement.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * Standardized error codes for machine-readable client logic.
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    RESOURCE_NOT_FOUND("ERR_RESOURCE_NOT_FOUND", HttpStatus.NOT_FOUND, "Resource not found"),
    VALIDATION_FAILED("ERR_VALIDATION_FAILED", HttpStatus.BAD_REQUEST, "Validation failed"),
    BUSINESS_RULE_VIOLATION("ERR_BUSINESS_RULE_VIOLATION", HttpStatus.BAD_REQUEST, "Business rule violated"),
    UNSUPPORTED_MEDIA_TYPE("ERR_UNSUPPORTED_MEDIA_TYPE", HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Unsupported media type"),
    CONFLICT("ERR_CONFLICT", HttpStatus.CONFLICT, "Conflict occurred"),
    INVALID_REQUEST_BODY("ERR_INVALID_REQUEST_BODY", HttpStatus.BAD_REQUEST, "Invalid request body"),
    INTERNAL_SERVER_ERROR("ERR_INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");

    private final String value;
    private final HttpStatus httpStatus;
    private final String defaultMessage;
}
