package com.wbt.todo_app.services.impl;

import com.wbt.todo_app.dto.TodoRequest;
import com.wbt.todo_app.exception.TodoAlreadyExistsException;
import com.wbt.todo_app.mapper.TodoMapper;
import com.wbt.todo_app.models.Todo;
import com.wbt.todo_app.repositories.TodoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

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
    void givenValidTodoRequest_CreateNewTodoWhichAlreadyExist_ThrowTodoAlreadyExistException() {
        // Given
        final var request = new TodoRequest(
                "Use Mockito for test",
                "Mock objects of test service",
                false
        );
        Mockito.when(repository.findByTitle(any(String.class)))
                .thenReturn(Optional.of(TodoMapper.toTodo(request)));

        // When // Then
        // Then we check that an exception will be thrown and 'save' method of the repository will never be called
        assertThatThrownBy(() -> this.underTest.create(request))
                .isInstanceOf(TodoAlreadyExistsException.class)
                .hasMessage("Todo with title: '%s' already exist", request.title());

        verify(repository, never()).save(any());
    }

    @Test
    void getAllTodos_ReturnAListOfTodoResponse() {
        // Given
        final var todo1 = new Todo(
                UUID.randomUUID(),
                "Use Mockito for test",
                "Mock objects of test service",
                false
        );
        final var todo2 = new Todo(
                UUID.randomUUID(),
                "Java 17",
                "Modern java features",
                true
        );
        final var todos = List.of(todo1, todo2);
        Mockito.when(repository.findAll()).thenReturn(todos);

        // When
        final var allTodos = this.underTest.getAllTodos();

        // Then
        assertThat(allTodos.size()).isEqualTo(2);
    }

    @Test
    void getAllTodos_WhenThereIsNotTodos_ReturnAnEmptyList() {
        // Given
        Mockito.when(repository.findAll()).thenReturn(List.of());

        // When
        final var todoList = this.underTest.getAllTodos();

        // Then
        assertThat(todoList).isEmpty();
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