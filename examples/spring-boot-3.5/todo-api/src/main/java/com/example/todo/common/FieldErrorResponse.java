package com.example.todo.common;

public record FieldErrorResponse(
        String field,
        String message
) {
}
