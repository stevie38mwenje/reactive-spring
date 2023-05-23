package com.example.todo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
public class Todo{
    @Id
    @Column(value = "id")
    private Long id;
    @Column(value = "name")
    private String name;
    @Column(value = "status")
    private Status status = Status.PENDING;
    @Column(value = "userId")
    private Long assignee;
    @CreatedDate
    @Column(value = "createdDate")
    private LocalDateTime createdDate;
    @LastModifiedDate
    @Column(value = "updatedDate")
    private LocalDateTime updatedDate;
    @Column(value = "teamId")
    private Long teamId;
    @Column(value = "code")
    private String code;

}
