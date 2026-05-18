package com.example.todo.todo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateTodoRequest(
        @NotBlank
        @Size(max = 200)
        String title,

        @Size(max = 1000)
        String description
) {
}
