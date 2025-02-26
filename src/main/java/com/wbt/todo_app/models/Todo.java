package com.wbt.todo_app.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "todos")
public class Todo {
    @Id
    @GeneratedValue
    private UUID id;
    private String title;
    private String description;
    private Boolean isDone = false;

    public Todo(String title, String description) {
        this.title = title;
        this.description = description;
        this.isDone = false;
    }
}
