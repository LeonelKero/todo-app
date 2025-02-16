package com.wbt.todo_app.controllers;

import com.wbt.todo_app.dto.TodoRequest;
import com.wbt.todo_app.dto.TodoResponse;
import com.wbt.todo_app.services.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<TodoResponse>> getTodos() {
        return new ResponseEntity<>(this.service.getAllTodos(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TodoResponse> add(final @RequestBody TodoRequest todoRequest) {
        return new ResponseEntity<>(this.service.create(todoRequest), HttpStatus.CREATED);
    }

    @GetMapping(path = {"/{id}"})
    public ResponseEntity<TodoResponse> getById(final @PathVariable UUID id) {
        return new ResponseEntity<>(this.service.fetchById(id), HttpStatus.OK);
    }

    @GetMapping(path = {"/get/{title}"})
    public ResponseEntity<TodoResponse> getById(final @PathVariable String title) {
        return new ResponseEntity<>(this.service.fetchByTitle(title), HttpStatus.OK);
    }

    @DeleteMapping(path = {"/{id}"})
    public ResponseEntity<Integer> remove(final @PathVariable UUID id) {
        return new ResponseEntity<>(this.service.deleteById(id), HttpStatus.OK);
    }

    @PutMapping(path = {"/{id}"})
    public ResponseEntity<TodoResponse> update(final @PathVariable UUID id, final @RequestBody TodoRequest requestTodo) {
        return new ResponseEntity<>(this.service.update(id, requestTodo), HttpStatus.ACCEPTED);
    }

    @PatchMapping(path = {"/{id}"})
    public ResponseEntity<TodoResponse> patchTodo(final @PathVariable UUID id, final @RequestBody TodoRequest request) {
        return new ResponseEntity<>(this.service.patch(id, request), HttpStatus.ACCEPTED);
    }
}
