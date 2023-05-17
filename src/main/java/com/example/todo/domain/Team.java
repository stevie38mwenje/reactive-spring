package com.example.todo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "teams")

public class Team {
    @Id
    private Long id;
    private String name;

}
