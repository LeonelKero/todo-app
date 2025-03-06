package com.wbt.todo_app.services.impl;

import com.wbt.todo_app.dto.TodoRequest;
import com.wbt.todo_app.dto.TodoResponse;
import com.wbt.todo_app.exception.TodoAlreadyExistsException;
import com.wbt.todo_app.exception.TodoNotFoundException;
import com.wbt.todo_app.mapper.TodoMapper;
import com.wbt.todo_app.models.Todo;
import com.wbt.todo_app.repositories.TodoRepository;
import com.wbt.todo_app.services.TodoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TodoServiceImpl implements TodoService {

    private final TodoRepository repository;

    public TodoServiceImpl(TodoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void createTodo(final TodoRequest todoRequest) {
        /*
        if (this.repository.findByTitle(todoRequest.title().trim().toLowerCase()).isPresent())
            throw new TodoAlreadyExistsException("Todo with title: '" + todoRequest.title() + "' already exist");

        final var newTodo = TodoMapper.toTodo(todoRequest);

        TodoMapper.toResponse(this.repository.save(newTodo));
         */
    }

    @Override
    public TodoResponse create(final TodoRequest todo) {
        if (this.repository.findByTitle(todo.title().trim().toLowerCase()).isPresent()) {
            //this.repository.save(new Todo());
            throw new TodoAlreadyExistsException("Todo with title: '" + todo.title() + "' already exist");
        }

        final var newTodo = TodoMapper.toTodo(todo);

        return TodoMapper.toResponse(this.repository.save(newTodo));
    }

    @Override
    public List<TodoResponse> getAllTodos() {
        return this.repository.findAll().stream()
                .map(TodoMapper::toResponse)
                .toList();
    }

    @Override
    public TodoResponse fetchById(final UUID todoId) {
        return this.repository.findById(todoId)
                .map(TodoMapper::toResponse)
                .orElseThrow(() -> new TodoNotFoundException("Todo not found for ID: " + todoId));
    }

    @Override
    public TodoResponse fetchByTitle(final String todoTitle) {
        return this.repository.findByTitle(todoTitle)
                .map(TodoMapper::toResponse)
                .orElseThrow(() -> new TodoNotFoundException("Todo not found with title: " + todoTitle));
    }

    @Override
    public Integer deleteById(UUID todoId) {
        return this.repository.findById(todoId)
                .map(todo -> {
                    this.repository.deleteById(todo.getId());
                    return 1;
                })
                .orElseThrow(() -> new TodoNotFoundException("Todo with id: " + todoId + " not found"));
    }

    @Override
    public TodoResponse update(final UUID todoId, final TodoRequest request) {
        if (this.repository.findByTitle(request.title().trim().toLowerCase()).isPresent())
            throw new TodoAlreadyExistsException("Cannot update with title: '" + request.title() + "' because it already exist");

        return this.repository.findById(todoId)
                .map(todo -> {
                    final var updateTodo = TodoMapper.toTodo(request);
                    updateTodo.setId(todoId);

                    //updateTodo.setTitle("fake title");

                    final var saved = this.repository.save(updateTodo);

                    return TodoMapper.toResponse(saved);
                })
                .orElseThrow(() -> new TodoNotFoundException("Todo with id: " + todoId + " not found"));
    }

    @Override
    public TodoResponse patch(final UUID todoId, final TodoRequest todo) {
        return this.repository.findById(todoId)
                .map(td -> {
                    if (todo.done() != null) td.setIsDone(todo.done());
                    if (todo.title() != null) td.setTitle(todo.title().trim());
                    if (todo.description() != null) td.setDescription(todo.description().trim());
                    return TodoMapper.toResponse(this.repository.save(td));
                }).orElseThrow(() -> new TodoNotFoundException("Todo with id: " + todoId + " not found"));
    }
}
