package com.example.todo.todo;

public class TodoNotFoundException extends RuntimeException {

    public TodoNotFoundException(Long id) {
        super("Todo를 찾을 수 없습니다. id=" + id);
    }
}
