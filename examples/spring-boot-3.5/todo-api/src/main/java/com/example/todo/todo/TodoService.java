package com.example.todo.todo;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Transactional
    public TodoResponse create(CreateTodoRequest request) {
        Todo todo = Todo.create(request.title(), request.description());
        Todo savedTodo = todoRepository.save(todo);
        return TodoResponse.from(savedTodo);
    }

    @Transactional(readOnly = true)
    public List<TodoResponse> findAll() {
        return todoRepository.findAll()
                .stream()
                .map(TodoResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public TodoResponse findById(Long id) {
        return TodoResponse.from(getTodo(id));
    }

    @Transactional
    public TodoResponse update(Long id, UpdateTodoRequest request) {
        Todo todo = getTodo(id);
        todo.update(request.title(), request.description());
        return TodoResponse.from(todo);
    }

    @Transactional
    public TodoResponse complete(Long id) {
        Todo todo = getTodo(id);
        todo.complete();
        return TodoResponse.from(todo);
    }

    @Transactional
    public void delete(Long id) {
        Todo todo = getTodo(id);
        todoRepository.delete(todo);
    }

    private Todo getTodo(Long id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException(id));
    }
}
