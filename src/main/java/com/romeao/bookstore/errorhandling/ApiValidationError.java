package com.romeao.bookstore.errorhandling;

public class ApiValidationError extends ApiSubError {
    private String field;
    private Object rejectedValue;
    private String message;

    public ApiValidationError(String field, String message, Object rejectedValue) {
        this.field = field;
        this.message = message;
        this.rejectedValue = rejectedValue;
    }

    public String getField() {
        return field;
    }

    public Object getRejectedValue() {
        return rejectedValue;
    }

    public String getMessage() {
        return message;
    }
}
