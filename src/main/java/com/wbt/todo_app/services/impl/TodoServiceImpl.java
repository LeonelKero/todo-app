package com.wbt.todo_app.services.impl;

import com.wbt.todo_app.exception.TodoNotFoundException;
import com.wbt.todo_app.models.Todo;
import com.wbt.todo_app.repositories.TodoRepository;
import com.wbt.todo_app.services.TodoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TodoServiceImpl implements TodoService {

    private final TodoRepository repository;

    public TodoServiceImpl(TodoRepository repository) {
        this.repository = repository;
    }

    @Override
    public Todo create(final Todo todo) {
        return this.repository.save(todo);
    }

    @Override
    public List<Todo> getAllTodos() {
        return this.repository.findAll();
    }

    @Override
    public Todo fetchById(final UUID todoId) {
        return this.repository.findById(todoId).orElse(null);
    }

    @Override
    public Todo fetchByTitle(final String todoTitle) {
        return this.repository.findByTitle(todoTitle).orElse(null);
    }

    @Override
    public Integer deleteById(UUID todoId) {
        return this.repository.findById(todoId)
                .map(todo -> {
                    this.repository.deleteById(todo.getId());
                    return 1;
                })
                .orElseThrow(() -> new TodoNotFoundException("Todo with id: " + todoId + " not found"));
    }

    @Override
    public Todo update(UUID todoId, Todo request) {
        return this.repository.findById(todoId)
                .map(todo -> this.repository.save(request))
                .orElseThrow(() -> new TodoNotFoundException("Todo with id: " + todoId + " not found"));
    }
}
