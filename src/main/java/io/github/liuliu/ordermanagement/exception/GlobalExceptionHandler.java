package io.github.liuliu.ordermanagement.exception;

import io.github.liuliu.model.ErrorDetail;
import io.github.liuliu.model.ErrorResponse;
import io.github.liuliu.ordermanagement.context.TraceIdContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String TRACE_HEADER = "X-Trace-Id";

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        return createResponse(ex.getErrorCode(), ex.getMessage(), HttpStatus.NOT_FOUND, null);
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ErrorResponse> handleBusinessRule(BusinessRuleException ex) {
        return createResponse(ex.getErrorCode(), ex.getMessage(), HttpStatus.BAD_REQUEST, null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        log.error("Unhandled exception occurred with traceId: {}", TraceIdContext.get(), ex);
        return createResponse(ErrorCode.INTERNAL_SERVER_ERROR, "An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR, null);
    }

    private ResponseEntity<ErrorResponse> createResponse(ErrorCode code, String message, HttpStatus status, List<ErrorDetail> details) {
        String traceId = TraceIdContext.get();
        
        ErrorResponse response = new ErrorResponse();
        response.setCode(code.getValue());
        response.setMessage(message);
        response.setTraceId(traceId);
        response.setDetails(details);

        HttpHeaders headers = new HttpHeaders();
        headers.set(TRACE_HEADER, traceId);

        return new ResponseEntity<>(response, headers, status);
    }
}
