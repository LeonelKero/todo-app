package com.wbt.todo_app.controllers;

import com.wbt.todo_app.models.Todo;
import com.wbt.todo_app.services.TodoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(path = {"/api/v1/todos"})
public class TodoController {

    private final TodoService service;

    public TodoController(TodoService service) {
        this.service = service;
    }

    @GetMapping
    public String getTodos() {
        return "Getting all todos!";
    }

    @PostMapping
    public Todo add() {
        return new Todo(
                UUID.randomUUID(),
                "Master Spring Boot",
                "Get hands on spring boot API development",
                false);
    }
}
