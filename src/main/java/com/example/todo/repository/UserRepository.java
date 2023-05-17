package com.example.todo.repository;


import com.example.todo.domain.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;


public interface UserRepository extends ReactiveCrudRepository<User,Long> {
}
