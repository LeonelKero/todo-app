package com.wbt.todo_app.dto;

public record TodoRequest(String title, String description, Boolean done) {
}
