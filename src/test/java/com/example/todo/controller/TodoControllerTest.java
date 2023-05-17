package com.example.todo.controller;

import com.example.todo.domain.Status;
import com.example.todo.domain.Todo;
import com.example.todo.domain.dto.request.TeamRequest;
import com.example.todo.domain.dto.request.TodoRequest;
import com.example.todo.domain.dto.request.UserRequest;
import com.example.todo.domain.dto.response.GenericResponse;
import com.example.todo.service.TodoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = TodoController.class)

class TodoControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TodoService todoService;

    @Test
    void createTodo() throws JsonProcessingException {
        TodoRequest todoRequest = TodoRequest.builder().name("run").assignee(1L).status(Status.DONE).build();
        GenericResponse response = GenericResponse.builder().data(todoRequest)
                .message("created todo successfully").build();
        when(todoService.createTodo(any(TodoRequest.class))).thenReturn(Mono.just(response));

        webTestClient
                .post().uri("/api/todo")
                .bodyValue(todoRequest)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void getTodos() {
        var todos = List.of(
                new Todo(1L, "Todo 1", Status.PENDING,2L, LocalDateTime.now(),LocalDateTime.now()),
                new Todo(2L, "Todo 2", Status.DONE,2L, LocalDateTime.now(),LocalDateTime.now())
        );

        GenericResponse mockResponse = new GenericResponse(todos, "Todos retrieved successfully", true, null);

        when(todoService.getTodos()).thenReturn(Flux.just(mockResponse));

        webTestClient
                .get().uri("/api/todo/all")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void getTodo() {
        TodoRequest todoRequest = TodoRequest.builder().name("run").assignee(1L).status(Status.DONE).build();
        var id = 1L;
        GenericResponse response = GenericResponse.builder().data(todoRequest)
                .message("created todo successfully").build();

        when(todoService.getTodo(id)).thenReturn(Mono.just(response));

        webTestClient
                .get().uri("/api/todo"+"/{id}", id)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(GenericResponse.class)
                .consumeWith(todoRequestEntityExchangeResult -> {
                    var fetchedTodo = todoRequestEntityExchangeResult.getResponseBody();
                    assert fetchedTodo!=null;
                    assert fetchedTodo.getData()!=null;
                    //assertEquals("run", updatedTodo.getData());
                });

        Mockito.verify(todoService).getTodo(1L);


    }

    @Test
    void updateTodo() {
        TodoRequest todoRequest = TodoRequest.builder().name("run").assignee(1L).status(Status.DONE).build();
        var id = 1L;
        GenericResponse response = GenericResponse.builder().data(todoRequest)
                .message("created todo successfully").build();

        when(todoService.updateTodo(isA(TodoRequest.class), isA(Long.class))).thenReturn(
                Mono.just(response)
        );

        webTestClient
                .put().uri("/api/todo"+"/{id}", id)
                .bodyValue(todoRequest)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(GenericResponse.class)
                .consumeWith(todoRequestEntityExchangeResult -> {
                    var updatedTodo = todoRequestEntityExchangeResult.getResponseBody();
                    assert updatedTodo!=null;
                    assert updatedTodo.getData()!=null;

                    try {
                        assertEquals(objectMapper.writeValueAsString(todoRequest), objectMapper.writeValueAsString(updatedTodo.getData()));
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    //assertEquals("run", updatedTodo.getData());
                });
    }

    @Test
    void createTeam() {
        TeamRequest teamRequest = TeamRequest.builder().name("DEV").build();
        GenericResponse response = GenericResponse.builder().data(teamRequest)
                .message("team created successfully").build();
        when(todoService.createTeam(any(TeamRequest.class))).thenReturn(Mono.just(response));

        webTestClient
                .post().uri("/api/team")
                .bodyValue(teamRequest)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void createUser() {
        //when
        UserRequest userRequest = UserRequest.builder().name("Steve").email("steviemwenje@gmail.com").teamId(1L).build();
        GenericResponse response = GenericResponse.builder().data(userRequest)
                .message("User created").build();
        when(todoService.createUser(userRequest)).thenReturn(Mono.just(response));
        //when(todoService.createUser(any(UserRequest.class))).thenReturn(Mono.just(response));

        //then
        webTestClient
                .post().uri("/api/user")
                .bodyValue(userRequest)
                .exchange()
                .expectStatus()
                .isOk();
    }
}