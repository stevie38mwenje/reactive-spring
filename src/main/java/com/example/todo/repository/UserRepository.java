package com.example.todo.repository;


import com.example.todo.domain.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;


public interface UserRepository extends ReactiveCrudRepository<User,Long> {
    @Query("SELECT COUNT(*) FROM team WHERE code = :code")
    Mono<Boolean> existsByCode(String code);
}
