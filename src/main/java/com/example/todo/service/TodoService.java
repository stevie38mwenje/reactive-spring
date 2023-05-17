package com.example.todo.service;

import com.example.todo.domain.dto.request.TeamRequest;
import com.example.todo.domain.dto.request.TodoRequest;
import com.example.todo.domain.dto.request.UserRequest;
import com.example.todo.domain.dto.response.GenericResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TodoService {
    Mono<GenericResponse> createTodo(TodoRequest todoRequest);

    Flux<GenericResponse> getTodos();

    Mono<GenericResponse> getTodo(Long id);

    Mono<GenericResponse> updateTodo(TodoRequest todoRequest, Long id);

    Mono<GenericResponse> createTeam(TeamRequest teamRequest);

    Mono<GenericResponse> createUser(UserRequest userRequest);
}
