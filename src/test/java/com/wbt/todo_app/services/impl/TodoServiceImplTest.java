package com.wbt.todo_app.services.impl;

import com.wbt.todo_app.dto.TodoRequest;
import com.wbt.todo_app.dto.TodoResponse;
import com.wbt.todo_app.exception.TodoAlreadyExistsException;
import com.wbt.todo_app.exception.TodoNotFoundException;
import com.wbt.todo_app.mapper.TodoMapper;
import com.wbt.todo_app.models.Todo;
import com.wbt.todo_app.repositories.TodoRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(value = MockitoExtension.class)
class TodoServiceImplTest {

    @Mock
    private TodoRepository repository;

    @InjectMocks
    private TodoServiceImpl underTest;

    @Captor
    private ArgumentCaptor<Todo> captor;


    @Test
    void givenValidTodoRequest_WhenCreateNewTodoWhichDoNotExist_ReturnTodoResponseObject() {
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
    void givenValidTodoRequest_WhenCreateNewTodoWhichAlreadyExist_ThrowTodoAlreadyExistException() {
        // Given
        final var request = new TodoRequest(
                "Use Mockito for test",
                "Mock objects of test service",
                false
        );
        Todo todo = TodoMapper.toTodo(request);
        Mockito.when(repository.findByTitle(any(String.class)))
                .thenReturn(Optional.of(todo));

        // When // Then
        // Then we check that an exception will be thrown and 'save' method of the repository will never be called
        assertThatThrownBy(() -> this.underTest.create(request))
                .isInstanceOf(TodoAlreadyExistsException.class)
                .hasMessage("Todo with title: '%s' already exist", request.title());

        verify(repository, never()).save(any());
    }

    @Test
    void getAllTodos_WhenGettingAllTodos_ReturnAListOfTodoResponse() {
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
    void givenAnExistingTodo_WhenFindingTodoById_ReturnTodoResponse() {
        // Given
        final var todoId = UUID.randomUUID();
        final var todo = new Todo(
                todoId,
                "Write test with Mockito",
                "Practice writing test for java application",
                false);
        Mockito.when(repository.findById(any(UUID.class)))
                .thenReturn(Optional.of(todo));

        // When
        final var response = this.underTest.fetchById(todoId);

        // Then
        assertThat(response).isInstanceOf(TodoResponse.class);
        assertThat(response.title()).isEqualTo(todo.getTitle());
    }

    @Test
    void givenNotExistingTodo_WhenFindingTodoById_ThrownTodoNotFoundException() {
        // Given
        final var fakeId = UUID.randomUUID();
        Mockito.when(repository.findById(any(UUID.class)))
                .thenReturn(Optional.empty());

        // When // Then
        assertThatThrownBy(() -> this.underTest.fetchById(fakeId))
                .isInstanceOf(TodoNotFoundException.class)
                .hasMessage("Todo not found for ID: %s", fakeId);
    }

    @Test
    void givenAnExistingTodo_WhenFindingTodoByTitle_ReturnTodoResponse() {
        // Given
        final var title = "Write test with Mockito";
        final var todo = new Todo(
                UUID.randomUUID(),
                title,
                "Practice writing test for java application",
                false);
        Mockito.when(repository.findByTitle(any(String.class)))
                .thenReturn(Optional.of(todo));

        // Then
        final var result = this.underTest.fetchByTitle(title);

        // When
        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(TodoResponse.class);
        assertThat(result.description()).isEqualTo(todo.getDescription());
    }

    @Test
    void givenNotExistingTodo_WhenFindingTodoByTitle_ThrownTodoNotFoundException() {
        // Given
        final var fakeTitle = "Sleep all day long";
        Mockito.when(repository.findByTitle(any(String.class)))
                .thenReturn(Optional.empty());

        // When // Then
        assertThatThrownBy(() -> this.underTest.fetchByTitle(fakeTitle))
                .isInstanceOf(TodoNotFoundException.class)
                .hasMessage("Todo not found with title: %s", fakeTitle);
    }

    @Test
    void givenAnExistingTodo_WhenDeleteById_ReturnTheNumberOfRowsAffected() {
        // Given
        final var todoId = UUID.randomUUID();
        final var todo = new Todo(
                todoId,
                "Write test with Mockito",
                "Practice writing test for java application",
                false);
        Mockito.when(repository.findById(any(UUID.class)))
                .thenReturn(Optional.of(todo));

        // When
        final var result = this.underTest.deleteById(todoId);

        // Then
        Mockito.verify(repository, times(1))
                .deleteById(any(UUID.class));
        assertThat(result).isEqualTo(1);
    }

    @Test
    void givenNotExistingTodo_WhenDeleteById_ThrownTodoNotFoundException() {
        // Given
        final var fakeId = UUID.randomUUID();
        when(repository.findById(any(UUID.class)))
                .thenReturn(Optional.empty());

        // When // Then
        assertThatThrownBy(() -> this.underTest.deleteById(fakeId))
                .isInstanceOf(TodoNotFoundException.class)
                .hasMessage("Todo with id: %s not found", fakeId);
        Mockito.verify(repository, times(0)).deleteById(any());
    }

    @Test
    void givenAnExistingTodo_WhenTryingToUpdateWithSameTitle_ThrownTodoAlreadyExistsException() {
        // Given
        final var title = "Learn Java 17";
        final var id = UUID.randomUUID();
        final var request = new TodoRequest(title, "Some description", false);
        final var todo = new Todo(
                id,
                title,
                "Practice writing unit test in java application",
                false);
        Mockito.when(repository.findByTitle(any(String.class)))
                .thenReturn(Optional.of(todo));
        // When // Then
        assertThatThrownBy(() -> this.underTest.update(id, request))
                .isInstanceOf(TodoAlreadyExistsException.class)
                .hasMessage("Cannot update with title: '%s' because it already exist", title);
        Mockito.verify(repository, never()).findById(any());
        Mockito.verify(repository, never()).save(any());
    }

    @Test
    void givenAnExistingTodo_WhenTryingToUpdateWithDifferentTielt_ThenUpdateAndReturnTodoResponse() {
        // Given
        final var id = UUID.randomUUID();
        final var request = new TodoRequest("Learn Java 23", "Some description", false);
        final var existingTodo = new Todo(
                id,
                "Learn Java 17",
                "Practice writing unit test in java application",
                false);
        final var updatedTodo = new Todo(id, request.title(), request.description(), request.done());

        Mockito.when(repository.findByTitle(any(String.class)))
                .thenReturn(Optional.empty());
        Mockito.when(repository.findById(any(UUID.class)))
                .thenReturn(Optional.of(existingTodo));
        Mockito.when(repository.save(any(Todo.class)))
                .thenReturn(updatedTodo);

        // When
        final var result = this.underTest.update(id, request);
        Mockito.verify(repository, times(1))
                .save(captor.capture());
        final var capturedTodo = captor.getValue();

        // Then
        assertThat(result).isInstanceOf(TodoResponse.class);
        assertThat(capturedTodo.getId()).isEqualTo(existingTodo.getId());
        assertThat(capturedTodo.getTitle()).isEqualTo(request.title());
        assertThat(capturedTodo.getDescription()).isEqualTo(request.description());
        assertThat(capturedTodo.getIsDone()).isEqualTo(request.done());
    }

    @Test
    void givenNotExistingTodo_WhenTryingToUpdateWithWrongId_ThrownTodoNotFoundException() {
        // Given
        final var fakeId = UUID.randomUUID();
        final var request = new TodoRequest("Learn Java 23", "Some description", false);
        Mockito.when(repository.findByTitle(any(String.class))).thenReturn(Optional.empty());
        Mockito.when(repository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // When // Then
        Mockito.verify(repository, never()).save(any(Todo.class));
        assertThatThrownBy(() -> this.underTest.update(fakeId, request))
                .isInstanceOf(TodoNotFoundException.class)
                .hasMessage("Todo with id: %s not found", fakeId);
    }

    @Test
    @Disabled
    void patch() {
    }
}