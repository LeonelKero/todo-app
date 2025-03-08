package com.wbt.todo_app.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wbt.todo_app.dto.TodoRequest;
import com.wbt.todo_app.dto.TodoResponse;
import com.wbt.todo_app.exception.TodoNotFoundException;
import com.wbt.todo_app.services.impl.TodoServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Disabled;
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
    @Disabled
    void givenValidTodoRequestToBeAdded_ThenReturnResponseEntityOfCreatedTodoWithStatus_CREATED() throws Exception {
        // Given
        final var request = new TodoRequest("JUnit 5", "Unit testing with JUnit", false);
        final var response = new TodoResponse(UUID.randomUUID(), "JUnit 5", "Unit testing with JUnit", false);
        Mockito.when(todoService.create(any(TodoRequest.class))).thenReturn(response);

        // When // Then
        mvc.perform(MockMvcRequestBuilders.post(API_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is(response.title())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is(response.description())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.done", Matchers.is(response.done())));
    }
    // TODO: Test invalid request object

    @Test
    void givenExistingTodo_GettingTodoById_ReturnCorrespondingTodoInResponseEntityWithStatus_OK() throws Exception {
        // Given
        final var todoId = UUID.randomUUID();
        final var response = new TodoResponse(todoId, "JUnit 5", "Unit testing with JUnit", false);
        Mockito.when(todoService.fetchById(any(UUID.class))).thenReturn(response);

        // When // Then
        mvc.perform(MockMvcRequestBuilders.get(API_PATH + "/{id}", todoId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is(response.title())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is(response.description())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.done", Matchers.is(response.done())));
    }

    @Test
    void givenNotExistingTodo_WhenGettingById_ThrowTodoNotFoundExceptionWithStatus_BAD_REQUEST() throws Exception {
        // Given
        final var fakeId = UUID.randomUUID();
        Mockito.when(todoService.fetchById(any(UUID.class)))
                .thenThrow(TodoNotFoundException.class);

        // When // Then
        mvc.perform(MockMvcRequestBuilders.get(API_PATH + "/{id}", fakeId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void givenExistingTodo_WhenGettingByTitle_ReturnTodoResponseInResponseEntityWithStatus_OK() throws Exception {
        // Given
        final var title = "Spring boot test";
        final var response = new TodoResponse(UUID.randomUUID(), title, "Ensure code quality with tests.", false);
        Mockito.when(todoService.fetchByTitle(any(String.class)))
                .thenReturn(response);

        // When // Then
        mvc.perform(MockMvcRequestBuilders.get(API_PATH + "/get/{title}", title))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.done", Matchers.notNullValue()));
    }

    @Test
    void givenNotExistingTodo_WhenGettingByTitle_ThrowTodoNotFoundExceptionWithStatus_BAD_REQUEST() throws Exception {
        // Given
        final var fakeTitle = "Swimming around java.";
        Mockito.when(todoService.fetchByTitle(any(String.class)))
                .thenThrow(TodoNotFoundException.class);

        // When // Then
        mvc.perform(MockMvcRequestBuilders.get(API_PATH + "/get/{title}", fakeTitle))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void givenExistingTodo_WhenTryingToRemoveById_ThenReturnResponseEntityOfAffectedElementsWithStatus_OK() throws Exception {
        // Given
        final var todoId = UUID.randomUUID();
        Mockito.when(todoService.deleteById(any(UUID.class))).thenReturn(1);

        // When // Then
        mvc.perform(MockMvcRequestBuilders.delete(API_PATH + "/{id}", todoId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.is(1)));
    }

    // TODO("Test to remove not existing todo")

    @Test
    void givenValidTodoRequest_WhenUpdateExistingTodo_ThenPerformUpdateAndReturnResponseEntityWithTodoResponseOnStatus_ACCEPTED() throws Exception {
        // Given
        final var todoId = UUID.randomUUID();
        final var request = new TodoRequest("Test", "Unit tests", true);
        final var response = new TodoResponse(todoId, "JUnit 5", "Unit tests", true);
        Mockito.when(todoService.update(any(UUID.class), any(TodoRequest.class)))
                .thenReturn(response);

        // When // Then
        mvc.perform(MockMvcRequestBuilders.put(API_PATH + "/{id}", todoId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isAccepted());
    }

    // TODO: Test in request with invalid object

    @Test
    void givenExistingTodo_WhenTryingToPatchTitleWithValidInputRequest_ThenReturnResponseEntityWithStatus_OK() throws Exception {
        // Given
        final var todoId = UUID.randomUUID();
        final var request = new TodoRequest("Junit 6", null, null);
        final var response = new TodoResponse(todoId, "Junit 6", "Keep testing", true);
        Mockito.when(todoService.patch(any(UUID.class), any(TodoRequest.class)))
                .thenReturn(response);

        // When // Then
        mvc.perform(MockMvcRequestBuilders.patch(API_PATH + "/{id}", todoId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is(request.title())));
    }
}