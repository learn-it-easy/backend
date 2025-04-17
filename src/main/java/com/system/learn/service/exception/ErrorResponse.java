package com.system.learn.service.exception;

public class ErrorResponse {
    private final String field;
    private final String message;
    private final Object rejectedValue;

    public ErrorResponse(String field, String message, Object rejectedValue) {
        this.field = field;
        this.message = message;
        this.rejectedValue = rejectedValue;
    }

    // Геттеры
    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }

    public Object getRejectedValue() {
        return rejectedValue;
    }
}