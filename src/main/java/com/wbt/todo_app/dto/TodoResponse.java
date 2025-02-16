package com.wbt.todo_app.dto;

import java.util.UUID;

public record TodoResponse(UUID id, String title, String description, Boolean done) {
}
