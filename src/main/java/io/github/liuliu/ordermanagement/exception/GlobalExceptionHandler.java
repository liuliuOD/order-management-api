package io.github.liuliu.ordermanagement.exception;

import tools.jackson.core.JacksonException;
import io.github.liuliu.model.ErrorDetail;
import io.github.liuliu.model.ErrorResponse;
import io.github.liuliu.ordermanagement.filter.RequestIdFilter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import tools.jackson.databind.exc.InvalidFormatException;
import tools.jackson.databind.exc.MismatchedInputException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final String TRACE_HEADER = RequestIdFilter.HEADER_NAME;

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        return createResponse(ex.getErrorCode(), ex.getMessage(), HttpStatus.NOT_FOUND, null);
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ErrorResponse> handleBusinessRule(BusinessRuleException ex) {
        return createResponse(ex.getErrorCode(), ex.getMessage(), HttpStatus.CONFLICT, null);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateKey(DuplicateKeyException ex) {
        log.warn("DuplicateKeyException occurred", ex);
        return createResponse(
                ErrorCode.BUSINESS_RULE_VIOLATION,
                "Request conflicts with existing data",
                HttpStatus.CONFLICT,
                null
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        log.warn("DataIntegrityViolationException occurred", ex);
        return createResponse(
                ErrorCode.BUSINESS_RULE_VIOLATION,
                "Request violates data integrity constraints",
                HttpStatus.CONFLICT,
                null
        );
    }

    @ExceptionHandler(CannotAcquireLockException.class)
    public ResponseEntity<ErrorResponse> handleCannotAcquireLock(CannotAcquireLockException ex) {
        log.warn("CannotAcquireLockException occurred", ex);
        return createResponse(
                ErrorCode.BUSINESS_RULE_VIOLATION,
                "Resource is currently busy, please retry later",
                HttpStatus.CONFLICT,
                null
        );
    }

    @ExceptionHandler(TransientDataAccessException.class)
    public ResponseEntity<ErrorResponse> handleTransientDataAccess(TransientDataAccessException ex) {
        log.error("TransientDataAccessException occurred", ex);
        return createResponse(
                ErrorCode.INTERNAL_SERVER_ERROR,
                "Temporary database error occurred, please retry later",
                HttpStatus.SERVICE_UNAVAILABLE,
                null
        );
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<ErrorResponse> handleTransactionSystem(TransactionSystemException ex) {
        log.error("TransactionSystemException occurred", ex);
        return createResponse(
                ErrorCode.INTERNAL_SERVER_ERROR,
                "Transaction failed unexpectedly",
                HttpStatus.INTERNAL_SERVER_ERROR,
                null
        );
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse> handleDataAccess(DataAccessException ex) {
        log.error("DataAccessException occurred", ex);
        return createResponse(
                ErrorCode.INTERNAL_SERVER_ERROR,
                "Database operation failed",
                HttpStatus.INTERNAL_SERVER_ERROR,
                null
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        log.info("MethodArgumentNotValidException error", ex);
        List<ErrorDetail> details = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> {
                    ErrorDetail detail = new ErrorDetail();
                    detail.setField(err.getField());
                    detail.setMessage(err.getDefaultMessage());
                    return detail;
                })
                .collect(Collectors.toList());

        return createResponse(ErrorCode.VALIDATION_FAILED, "Validation failed", HttpStatus.BAD_REQUEST, details);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        ErrorDetail detail = new ErrorDetail();
        detail.setField(ex.getName());
        detail.setMessage("Invalid value '%s' for type %s".formatted(
                ex.getValue(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown"
        ));

        return createResponse(
                ErrorCode.VALIDATION_FAILED,
                "Validation failed",
                HttpStatus.BAD_REQUEST,
                List.of(detail)
        );
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex) {
        return createResponse(
                ErrorCode.UNSUPPORTED_MEDIA_TYPE,
                "Content-Type is not supported",
                HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                null
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        log.error("handleHttpMessageNotReadable exception occurred", ex);

        List<ErrorDetail> details = extractHttpMessageNotReadableDetails(ex);

        return createResponse(
                ErrorCode.INVALID_REQUEST_BODY,
                "Request body is missing or malformed",
                HttpStatus.BAD_REQUEST,
                details
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        log.error("Unhandled exception occurred with exception: {}", ex.getClass().getName(), ex);
        return createResponse(ErrorCode.INTERNAL_SERVER_ERROR, "An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR, null);
    }

    private List<ErrorDetail> extractHttpMessageNotReadableDetails(HttpMessageNotReadableException ex) {
        Throwable rootCause = getRootCause(ex);

        if (rootCause instanceof InvalidFormatException invalidFormatException) {
            ErrorDetail detail = new ErrorDetail();
            detail.setField(buildFieldPath(invalidFormatException.getPath()));
            detail.setMessage("Invalid value '%s' for type %s".formatted(
                    invalidFormatException.getValue(),
                    invalidFormatException.getTargetType() != null
                            ? invalidFormatException.getTargetType().getSimpleName()
                            : "unknown"
            ));
            return List.of(detail);
        }

        if (rootCause instanceof MismatchedInputException mismatchedInputException) {
            ErrorDetail detail = new ErrorDetail();
            detail.setField(buildFieldPath(mismatchedInputException.getPath()));
            detail.setMessage("Malformed or missing value for request field");
            return List.of(detail);
        }

        ErrorDetail detail = new ErrorDetail();
        detail.setField("requestBody");
        detail.setMessage("Malformed JSON request body");
        return List.of(detail);
    }

    private Throwable getRootCause(Throwable throwable) {
        Throwable current = throwable;
        while (current.getCause() != null && current.getCause() != current) {
            current = current.getCause();
        }
        return current;
    }

    private String buildFieldPath(List<JacksonException.Reference> path) {
        if (path == null || path.isEmpty()) {
            return "requestBody";
        }

        return path.stream()
                .map(ref -> ref.getPropertyName() != null ? ref.getPropertyName() : String.valueOf(ref.getIndex()))
                .collect(Collectors.joining("."));
    }

    private ResponseEntity<ErrorResponse> createResponse(ErrorCode code, String message, HttpStatus status, List<ErrorDetail> details) {
        String requestId = resolveRequestId();

        ErrorResponse response = new ErrorResponse();
        response.setCode(code.getValue());
        response.setMessage(message);
        response.setRequestId(requestId);
        response.setDetails(details);

        HttpHeaders headers = new HttpHeaders();
        headers.set(TRACE_HEADER, requestId);

        return new ResponseEntity<>(response, headers, status);
    }

    private String resolveRequestId() {
        String requestId = MDC.get(RequestIdFilter.MDC_KEY);
        if (requestId != null && !requestId.isBlank()) {
            return requestId;
        }

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            Object attr = attributes.getRequest().getAttribute(RequestIdFilter.REQUEST_ATTR_KEY);
            if (attr instanceof String attrValue && !attrValue.isBlank()) {
                return attrValue;
            }
        }

        return UUID.randomUUID().toString();
    }
}
