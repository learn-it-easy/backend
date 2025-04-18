package com.system.learn.service.exception;

public class ErrorResponseDto {
    private final String field;
    private final String message;

    public ErrorResponseDto(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }

}