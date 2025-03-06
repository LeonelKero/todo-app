package com.wbt.todo_app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TodoRequest(
        @NotBlank(message = "Todo title is required")
        @Size(min = 3, message = "Todo title should be at least 03 characters")
        String title,

        @NotBlank(message = "Todo description is required")
        String description,

        @NotNull(message = "Todo state is required. done: true | false")
        Boolean done) {
}
