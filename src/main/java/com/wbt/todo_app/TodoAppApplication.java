package com.wbt.todo_app;

import com.wbt.todo_app.models.Todo;
import com.wbt.todo_app.repositories.TodoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class TodoAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodoAppApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(TodoRepository todoRepository) {
        final var t1 = new Todo("Spring App Development", "Learn how to build enterprise application in Spring Boot");
        final var t2 = new Todo("Tests with JUnit", "Hands on unit tests");
        final var t3 = new Todo("Advance tests with Mockito", "Advance test with mockito framework");
        return args -> {
            todoRepository.saveAll(Arrays.asList(t1, t2, t3));
        };
    }
}
