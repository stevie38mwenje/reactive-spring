package com.example.todo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class User {
    @Id
    @Column(value = "id")
    private Long id;
    @Column(value = "name")
    private String name;
    @Column(value = "email")
    private String email;
    @Column(value = "teamId")
    private Long teamId;
    @Column(value = "code")
    private String code;

}
