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
        this.underTest.save(new Todo("Master JUnit5 Test", "Get hands on application testing"));
    }

    @Test
    void findByTitle() {
        // Given
        final var title = "Master JUnit5 Test";
        // When
        final var result = this.underTest.findByTitle(title);
        // Then
        assertThat(result.isPresent()).isTrue();
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