package io.github.liuliu.ordermanagement.exception;

import lombok.Getter;

/**
 * Base exception for all API-related errors.
 */
@Getter
public abstract class ApiException extends RuntimeException {
    private final ErrorCode errorCode;

    protected ApiException(ErrorCode errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
    }

    protected ApiException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
