package com.example.todo.service;

import com.example.todo.domain.Team;
import com.example.todo.domain.Todo;
import com.example.todo.domain.User;
import com.example.todo.domain.dto.request.TeamRequest;
import com.example.todo.domain.dto.request.TodoRequest;
import com.example.todo.domain.dto.request.UserRequest;
import com.example.todo.domain.dto.response.GenericResponse;
import com.example.todo.exception.CustomException;
import com.example.todo.repository.TeamRepository;
import com.example.todo.repository.TodoRepository;
import com.example.todo.repository.UserRepository;
import com.example.todo.utils.CodeGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.r2dbc.UncategorizedR2dbcException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService{

    private final TodoRepository todoRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final RabbitMqSender rabbitMqSender;
    private final CodeGenerator codeGenerator;



    @Override
    public Mono<GenericResponse> createTodo(TodoRequest todoRequest) {
        //validations for data on the request payload
        var todo= Todo.builder()
                .name(todoRequest.getName())
                .status(todoRequest.getStatus())
                .assignee(todoRequest.getAssignee())
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();
        log.info("Persisting Todo data :{}",todo);
        return todoRepository.save(todo).
                map(savedTodo ->{
                    var res = GenericResponse.builder().success(true).message("created todo successfully").data(todo).build();
                    log.info("Todo response data :{}",savedTodo);
                    rabbitMqSender.send(savedTodo);
                    return res;});
    }
    @Override
    public Flux<GenericResponse> getTodos() {
        //TODO : Pagination
        return todoRepository.findAll()
                .collectList()
                .flatMapMany(todos ->{
                    var res = GenericResponse.builder()
                            .message("Todos retrieved successfully")
                            .data(todos)
                            .success(true)
                            .build();
                    return Flux.just(res);
                })
                .defaultIfEmpty(GenericResponse.builder().message("todos list is empty").data(null).build());
    }

    @Override
    public Mono<GenericResponse> getTodo(Long id) {
        return todoRepository.findById(id).switchIfEmpty(Mono.error(new CustomException("Todo not found")))
                .map(todo -> {
                    var response = GenericResponse.builder()
                                    .message("Todo fetched successfully")
                                    .data(todo)
                                    .success(true)
                                    .build();
        log.info("fetched todo: {}",todo);
        return response;});
    }

    @Override
    public Mono<GenericResponse> updateTodo(TodoRequest todoRequest, Long id) {
        //TODO : dates in UTC
        return todoRepository.findById(id).switchIfEmpty(Mono.error(new CustomException("Todo not found"))).
                flatMap(savedTodo ->{
                    savedTodo.setName(todoRequest.getName());
                    savedTodo.setStatus(todoRequest.getStatus());
                    savedTodo.setAssignee(todoRequest.getAssignee());
                    savedTodo.setUpdatedDate(LocalDateTime.now());
                    savedTodo.setCreatedDate(savedTodo.getCreatedDate());
                    log.info("updated payload : {}",savedTodo);

                    return todoRepository.save(savedTodo).
                            map(updatedTodo ->{
                                var res = GenericResponse.builder().success(true).message("updated todo successfully").status(HttpStatus.OK.value()).data(updatedTodo).build();
                                log.info("persisted todo: {}",updatedTodo);
                                rabbitMqSender.send(updatedTodo);
                                return res;});
                            });

    }

    @Override
    public Mono<GenericResponse> createTeam(TeamRequest teamRequest) {
        //TODO: ERROR HANDLING
        var generatedCode = codeGenerator.generateRandomAlphanumeric();

        var team = Team.builder().name(teamRequest.getName()).code(generatedCode).build();
        log.info("TEAM CREATION REQUEST : {}",team);
        return teamRepository.save(team)
                .map(newTeam->{ log.info("TEAM CREATION RESPONSE : {}",newTeam);
                    var res = GenericResponse.builder().data(newTeam).message("team created successfully").status(HttpStatus.CREATED.value()).success(true).build();
                    return res;}
                       ).onErrorResume(UncategorizedR2dbcException.class, e ->{
                    var res = GenericResponse.builder().data(null)
                            .message("Duplicate code for team, team already exist")
                            .success(false)
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .build();
                    return Mono.just(res);});
    }

    @Override
    public Mono<GenericResponse> createUser(UserRequest userRequest) {
        //TODO: ERROR HANDLING
        var newUser = User.builder().name(userRequest.getName()).teamId(userRequest.getTeamId()).email(userRequest.getEmail()).code(codeGenerator.generateRandomAlphanumeric()).build();
        log.info("Persisting user: {}",newUser);
        return this.userRepository.save(newUser)
                .map(user->{
                    var response = GenericResponse.builder().data(user).message("user created successfully").status(HttpStatus.CREATED.value()).success(true).build();
                    log.info("USER CREATION RESPONSE DATA : {}",user);
                    return response;
                })
                .onErrorResume(UncategorizedR2dbcException.class, e ->{
                var res = GenericResponse.builder().data(null)
                .message("Cannot add to a non existent group")
                .success(false)
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();
                return Mono.just(res);});
    }


}
