package com.wbt.todo_app.controllers;

import com.wbt.todo_app.models.Todo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(path = {"/api/v1/todos"})
public class TodoController {

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
