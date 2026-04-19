package io.github.liuliu.ordermanagement.exception;

public class BusinessRuleException extends ApiException {

    public BusinessRuleException() {
        super(ErrorCode.BUSINESS_RULE_VIOLATION);
    }

    public BusinessRuleException(String message) {
        super(ErrorCode.BUSINESS_RULE_VIOLATION, message);
    }
}
