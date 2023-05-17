package com.example.todo.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "todos")
public class Todo {
    @Id
    private Long id;

    private String name;
    private Status status = Status.PENDING;
    @Column(value = "user_id")
    private Long assignee;
    @CreatedDate
    @Column(value = "created_date")
    private LocalDateTime createdDate;
    @LastModifiedDate
    @Column(value = "updated_date")
    private LocalDateTime updatedDate;

}
