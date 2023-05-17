package com.example.todo.service;

import com.example.todo.domain.Status;
import com.example.todo.domain.Team;
import com.example.todo.domain.Todo;
import com.example.todo.domain.User;
import com.example.todo.domain.dto.request.TeamRequest;
import com.example.todo.domain.dto.request.TodoRequest;
import com.example.todo.domain.dto.request.UserRequest;
import com.example.todo.domain.dto.response.GenericResponse;
import com.example.todo.repository.TeamRepository;
import com.example.todo.repository.TodoRepository;
import com.example.todo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
@MockitoSettings(strictness = Strictness.LENIENT)
public class TodoServiceImplTest {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TodoServiceImpl todoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void createTodoShouldReturnGenericResponse() {

        // given
        String name = "run";
        Long assignee = 1L;
        Status status = Status.PENDING;
        TodoRequest todoRequest = new TodoRequest(name,assignee,status);
        Long id  = 1L;

        // when
        var persistedTodo = todoRepository.findById(id);
        Mono<GenericResponse> createUserResult = todoService.createTodo(todoRequest);


        // Then
        StepVerifier.create(createUserResult)
                .assertNext(response -> {
                    assertNotNull(response);
                })
                .verifyComplete();

        StepVerifier.create(persistedTodo)
                .assertNext(persistedData -> {
                    assertNotNull(persistedData.getId());
                    assertNotNull(persistedData.getName());
                    assertEquals(todoRequest.getName(), persistedData.getName());
                })
                .verifyComplete();
    }

    @Test
    void getTodos() {

        Flux<GenericResponse> result = todoService.getTodos();

        StepVerifier.create(result)
                .assertNext(response -> {
                    assertNotNull(response);
                })
                .verifyComplete();

    }


    @Test
    void getTodo() {

        var todo = new Todo(1L, "Todo 1", Status.PENDING,2L, LocalDateTime.now(),LocalDateTime.now());

        GenericResponse resp = GenericResponse.builder().data(todo)
                .message("Todo fetched successfully").success(true).build();
        Mono<GenericResponse> result = todoService.getTodo(1L);

        StepVerifier.create(result)
                .expectNextMatches(response -> {
                    assertEquals("Todo fetched successfully", resp.getMessage());
                    assertEquals(true, resp.isSuccess());
                    assertEquals(todo, resp.getData());
                    return true;
                })
                .verifyComplete();

    }

    @Test
    void updateTodo() {
    }

    @Test
    void createTeam() {
        // Given
        TeamRequest teamRequest = new TeamRequest();
        teamRequest.setName("DEV");
        var id = 1L;

        // When
        Mono<GenericResponse> createTeamResult = todoService.createTeam(teamRequest);
        Mono<Team> persistedDataResult = teamRepository.findById(id);

        // Then
        StepVerifier.create(createTeamResult)
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals(HttpStatus.CREATED.value(), response.getStatus());
                })
                .verifyComplete();

        StepVerifier.create(persistedDataResult)
                .assertNext(persistedData -> {
                    assertNotNull(persistedData.getId());
                    assertNotNull(persistedData.getName());
                    assertEquals(teamRequest.getName(), persistedData.getName());
                })
                .verifyComplete();
    }



    @Test
    void createUser() {
        // Given
        UserRequest userRequest = new UserRequest();
        userRequest.setName("Steve Mwenje");
        userRequest.setEmail("Mwenje@cellulant.io");
        userRequest.setTeamId(2L);
        var id = 1L;

        // When
        Mono<GenericResponse> createUserResult = todoService.createUser(userRequest);
        Mono<User> persistedDataResult = userRepository.findById(id);

        // Then
        StepVerifier.create(createUserResult)
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals(HttpStatus.CREATED.value(), response.getStatus());
                })
                .verifyComplete();

        StepVerifier.create(persistedDataResult)
                .assertNext(persistedData -> {
                    assertNotNull(persistedData.getId());
                    assertNotNull(persistedData.getEmail());
                    assertEquals(userRequest.getName(), persistedData.getName());
                })
                .verifyComplete();
    }
}
