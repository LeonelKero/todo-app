package com.wbt.todo_app.repositories;

import com.wbt.todo_app.models.Todo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TodoRepositoryTest {

    @Autowired
    private TodoRepository underTest;

    @BeforeEach
    void setUp() {
        this.underTest.save(new Todo(
                "Master JUnit5 Test",
                "Get hands on application testing"));
    }

    @Test
    void whenDataIsIntoDatabase_FindingByTitle_ReturnValueInOptional() {
        // Given
        final var title = "Master JUnit5 Test";

        // When
        final var result = this.underTest.findByTitle(title);

        // Then
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getTitle()).isEqualTo(title);
    }

    @Test
    void whenDataIsNotIntoDatabase_FindingByTitle_ReturnEmptyOptional() {
        // Given
        final var fakeTitle = "Not existing title";

        // When
        final var response = this.underTest.findByTitle(fakeTitle);

        // Then
        assertThat(response.isEmpty()).isTrue();
    }

    @Test
    void withValidTodoId_FindingById_ReturnTodoObject() {
        // Given
        final var newTodo = new Todo(
                "Lean Spring Boot 3",
                "Hands on enterprise application development.");
        final var id = this.underTest.save(newTodo).getId();

        // When
        final var optionalTodo = this.underTest.findById(id);

        // Then
        assertThat(optionalTodo.isPresent()).isTrue();
        assertThat(optionalTodo.get().getTitle()).isEqualTo(newTodo.getTitle());
        assertThat(optionalTodo.get().getDescription()).isEqualTo(newTodo.getDescription());
        assertThat(optionalTodo.get().getIsDone()).isFalse();
    }

    @Test
    void shouldSaveValidTodo() {
        // Given
        final var originalTitle = "Run some tests";
        final var newTodo = new Todo(originalTitle, "Run few test for code quality");
        // When
        final var savedTodo = this.underTest.save(newTodo);
        savedTodo.setTitle("New title");
        // Then
        assertThat(savedTodo.getTitle()).isNotEqualTo(originalTitle);
    }
}