package com.example.todo.common;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
        String code,
        String message,
        List<FieldErrorResponse> fieldErrors,
        LocalDateTime timestamp
) {

    public static ErrorResponse of(String code, String message) {
        return new ErrorResponse(code, message, List.of(), LocalDateTime.now());
    }

    public static ErrorResponse of(String code, String message, List<FieldErrorResponse> fieldErrors) {
        return new ErrorResponse(code, message, fieldErrors, LocalDateTime.now());
    }
}
