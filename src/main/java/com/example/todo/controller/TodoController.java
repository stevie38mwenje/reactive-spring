package com.example.todo.controller;

import com.example.todo.domain.dto.request.TeamRequest;
import com.example.todo.domain.dto.request.TodoRequest;
import com.example.todo.domain.dto.request.UserRequest;
import com.example.todo.domain.dto.response.GenericResponse;
import com.example.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/")
public class TodoController {
    private final TodoService todoService;
    @PostMapping("todo")
    public Mono<GenericResponse> createTodo(@RequestBody TodoRequest todoRequest){
        return todoService.createTodo(todoRequest);
    }
    @GetMapping("todo/all")
    public Flux<GenericResponse> getTodos(){
        return todoService.getTodos();
    }
    @GetMapping("todo/{id}")
    public Mono<GenericResponse> getTodo(@PathVariable(name = "id") Long id){
        return todoService.getTodo(id);
    }
    @PutMapping("todo/{id}")
    public Mono<GenericResponse> updateTodo(@RequestBody TodoRequest todoRequest, @PathVariable(name = "id") Long id){
        return todoService.updateTodo(todoRequest,id);
    }

    @PostMapping("team")
    public Mono<GenericResponse> createTeam(@RequestBody TeamRequest teamRequest){
        return todoService.createTeam(teamRequest);
    }


    @PostMapping("user")
    public Mono<GenericResponse> createUser(@RequestBody UserRequest userRequest){
        return todoService.createUser(userRequest);
    }


}
