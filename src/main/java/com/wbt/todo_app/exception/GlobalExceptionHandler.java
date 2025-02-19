package com.wbt.todo_app.exception;

import com.wbt.todo_app.exception.dto.ApiErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(exception = {TodoNotFoundException.class})
    public ResponseEntity<ApiErrorResponse> todoNotFoundExceptionHandler(final TodoNotFoundException ex, final WebRequest webRequest) {
        final var error = new ApiErrorResponse(webRequest.getDescription(false),
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(exception = {TodoAlreadyExistsException.class})
    public ResponseEntity<?> todoAlreadyExistException(final TodoAlreadyExistsException ex, final WebRequest webRequest) {
        return handleExceptionInternal(
                ex,
                new ApiErrorResponse(
                        webRequest.getDescription(false),
                        HttpStatus.BAD_REQUEST,
                        ex.getMessage(),
                        LocalDateTime.now()),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST,
                webRequest
        );
    }
}
