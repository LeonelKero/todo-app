package com.wbt.todo_app.mapper;

import com.wbt.todo_app.dto.TodoRequest;
import com.wbt.todo_app.dto.TodoResponse;
import com.wbt.todo_app.models.Todo;

public class TodoMapper {
    public static TodoResponse toResponse(final Todo todo) {
        return new TodoResponse(
                todo.getId(),
                todo.getTitle().trim(),
                todo.getDescription().trim(),
                todo.getIsDone()
        );
    }

    public static Todo toTodo(final TodoRequest request) {
        return new Todo(request.title(), request.description(), request.done());
    }
}
