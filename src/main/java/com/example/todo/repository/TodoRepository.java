package com.example.todo.repository;

import com.example.todo.domain.Todo;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;


public interface TodoRepository extends ReactiveCrudRepository<Todo,Long> {
}
