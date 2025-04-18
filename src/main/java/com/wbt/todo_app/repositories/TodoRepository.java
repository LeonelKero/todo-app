package com.wbt.todo_app.repositories;

import com.wbt.todo_app.models.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TodoRepository extends JpaRepository<Todo, UUID> {
    Optional<Todo> findByTitle(String title);
}
