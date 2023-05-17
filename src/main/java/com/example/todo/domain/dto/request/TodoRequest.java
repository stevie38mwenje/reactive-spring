package com.example.todo.domain.dto.request;

import com.example.todo.domain.Status;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class TodoRequest {
    private String name;
    private Long assignee;
    private Status status;

}
