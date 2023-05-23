package com.example.todo.repository;

import com.example.todo.domain.Todo;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;


public interface TodoRepository extends ReactiveCrudRepository<Todo,Long> {
    @Query("SELECT COUNT(*) FROM team WHERE code = :code")
    Mono<Boolean> existsByCode(String code);
}
