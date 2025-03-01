package com.wbt.todo_app.mapper;

import com.wbt.todo_app.dto.TodoRequest;
import com.wbt.todo_app.dto.TodoResponse;
import com.wbt.todo_app.models.Todo;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TodoMapperTest {

    @Test
    void givenValidTodEntity_ShouldReturnValidTodoResponse() {
        // Given
        final var todo = new Todo(
                UUID.randomUUID(),
                "Learn Java 17",
                "Practice writing unit test in java application",
                false);
        // When
        final var response = TodoMapper.toResponse(todo);
        // Then
        assertThat(response).isInstanceOf(TodoResponse.class);
        assertThat(response.id()).isEqualTo(todo.getId());
        assertThat(response.title()).isEqualTo(todo.getTitle());
        assertThat(response.description()).isEqualTo(todo.getDescription());
        assertThat(response.done()).isEqualTo(todo.getIsDone());
    }

    @Test
    void givenValidTodoRequest_ShouldReturnTodoEntityWithTitleAndDescription() {
        // Given
        final var request = new TodoRequest("JUnit", "Hands on application testing with JUnit 5", false);
        // When
        final var entity = TodoMapper.toTodo(request);
        // Then
        assertThat(entity).isInstanceOf(Todo.class);
        assertThat(entity.getId()).isNull();;
        assertThat(entity.getTitle()).isEqualTo(request.title());
        assertThat(entity.getDescription()).isEqualTo(request.description());
        assertThat(entity.getIsDone()).isEqualTo(request.done());
    }
}