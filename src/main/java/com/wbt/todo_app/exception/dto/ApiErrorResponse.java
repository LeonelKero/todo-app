package com.wbt.todo_app.exception.dto;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ApiErrorResponse(String path, HttpStatus status, String message, LocalDateTime timestamp) {
}
