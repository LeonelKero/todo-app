package com.wbt.todo_app.services;

import com.wbt.todo_app.models.Todo;

import java.util.List;
import java.util.UUID;

public interface TodoService {
    Todo create(final Todo todo);

    List<Todo> getAllTodos();

    Todo fetchById(final UUID todoId);

    Todo fetchByTitle(final String todoTitle);

    Integer deleteById(final UUID todoId);

    Todo update(final UUID todoId, final Todo todo);
}
