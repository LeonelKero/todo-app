package com.wbt.todo_app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wbt.todo_app.dto.TodoRequest;
import com.wbt.todo_app.dto.TodoResponse;
import com.wbt.todo_app.services.impl.TodoServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(controllers = {TodoController.class})
class TodoControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TodoServiceImpl todoService;

    private final static String API_PATH = "/api/v1/todos";

    @Test
    void givenExistingTodos_WhenGetAllTodos_ThenReturnResponseEntityOfTodoListWithStatus_OK() throws Exception {
        // Given
        final var r1 = new TodoResponse(UUID.randomUUID(), "Learn Spring Boot 3", "Hands on enterprise application development with spring boot", true);
        final var r2 = new TodoResponse(UUID.randomUUID(), "Quality Code", "Testing code to ensure its quality", false);
        Mockito.when(todoService.getAllTodos()).thenReturn(List.of(r1, r2));

        // When // Then
        mvc.perform(MockMvcRequestBuilders.get(API_PATH))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(2)));
    }


    @Test
    void givenValidTodoRequestToBeAdded_ThenReturnResponseEntityOfCreatedTodoWithStatus_CREATED() throws Exception {
        // Given
        final var request = new TodoRequest("JUnit 5", "Unit testing with JUnit", false);
        final var response = new TodoResponse(UUID.randomUUID(), "JUnit 5", "Unit testing with JUnit", false);
        Mockito.when(todoService.create(any(TodoRequest.class))).thenReturn(response);

        // When // Then
        mvc.perform(MockMvcRequestBuilders.post(API_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is(request.title())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is(request.description())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.done", Matchers.is(request.done())));
    }

    @Test
    void getById() {
    }

    @Test
    void testGetById() {
    }

    @Test
    void remove() {
    }

    @Test
    void update() {
    }

    @Test
    void patchTodo() {
    }
}