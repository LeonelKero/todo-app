package com.wbt.todo_app.services.impl;

import com.wbt.todo_app.repositories.TodoRepository;
import com.wbt.todo_app.services.TodoService;
import org.springframework.stereotype.Service;

@Service
public class TodoServiceImpl implements TodoService {

    private final TodoRepository repository;

    public TodoServiceImpl(TodoRepository repository) {
        this.repository = repository;
    }
}
