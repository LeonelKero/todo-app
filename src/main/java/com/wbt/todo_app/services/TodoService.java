package com.wbt.todo_app.services;

import com.wbt.todo_app.dto.TodoRequest;
import com.wbt.todo_app.dto.TodoResponse;

import java.util.List;
import java.util.UUID;

public interface TodoService {
    void createTodo(final TodoRequest todo);

    TodoResponse create(final TodoRequest todo);

    List<TodoResponse> getAllTodos();

    TodoResponse fetchById(final UUID todoId);

    TodoResponse fetchByTitle(final String todoTitle);

    Integer deleteById(final UUID todoId);

    TodoResponse update(final UUID todoId, final TodoRequest todo);

    TodoResponse patch(final UUID todoId, final TodoRequest todo);
}
