package com.wbt.todo_app.controllers;

import com.wbt.todo_app.models.Todo;
import com.wbt.todo_app.services.TodoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = {"/api/v1/todos"})
public class TodoController {

    private final TodoService service;

    public TodoController(TodoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Todo> getTodos() {
        return this.service.getAllTodos();
    }

    @PostMapping
    public Todo add(final @RequestBody Todo todoRequest) {
        return this.service.create(todoRequest);
    }

    @GetMapping(path = {"/{id}"})
    public Todo getById(final @PathVariable UUID id) {
        return this.service.fetchById(id);
    }

    @GetMapping(path = {"/get/{title}"})
    public Todo getById(final @PathVariable String title) {
        return this.service.fetchByTitle(title);
    }

    @DeleteMapping(path = {"/{id}"})
    public void remove(final @PathVariable UUID id) {
        this.service.deleteById(id);
    }

    @PutMapping(path = {"/{id}"})
    public Todo update(final @PathVariable UUID id, final @RequestBody Todo requestTodo) {
        return this.service.update(id, requestTodo);
    }
}
