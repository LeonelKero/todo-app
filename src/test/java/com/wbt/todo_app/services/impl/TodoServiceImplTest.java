package com.wbt.todo_app.services.impl;

import com.wbt.todo_app.dto.TodoRequest;
import com.wbt.todo_app.mapper.TodoMapper;
import com.wbt.todo_app.models.Todo;
import com.wbt.todo_app.repositories.TodoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(value = MockitoExtension.class)
class TodoServiceImplTest {

    @Mock
    private TodoRepository repository;

    @InjectMocks
    private TodoServiceImpl underTest;


    @Test
    void givenValidTodoRequest_CreateNewTodoWhichDoNotExist_ReturnTodoResponseObject() {
        // Given
        final var request = new TodoRequest(
                "Use Mockito for test",
                "Mock objects of test service",
                false
        );
        final var todo = TodoMapper.toTodo(request);
        todo.setId(UUID.randomUUID());

        Mockito.when(repository.findByTitle(any(String.class)))
                .thenReturn(Optional.empty());
        Mockito.when(repository.save(any(Todo.class)))
                .thenReturn(todo);

        // When
        final var result = this.underTest.create(request);

        // Then
        assertThat(result.id()).isNotNull();
        assertThat(result.title()).isEqualTo(request.title());
        assertThat(result.description()).isEqualTo(request.description());
    }

    @Test
    void getAllTodos() {
    }

    @Test
    void fetchById() {
    }

    @Test
    void fetchByTitle() {
    }

    @Test
    void deleteById() {
    }

    @Test
    void update() {
    }

    @Test
    void patch() {
    }
}