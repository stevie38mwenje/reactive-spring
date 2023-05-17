package com.example.todo.repository;

import com.example.todo.domain.Team;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;


public interface TeamRepository extends ReactiveCrudRepository<Team,Long> {
}
