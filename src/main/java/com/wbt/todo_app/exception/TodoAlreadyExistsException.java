package com.wbt.todo_app.exception;

public class TodoAlreadyExistsException extends RuntimeException {
    public TodoAlreadyExistsException(String message) {
        super(message);
    }
}
