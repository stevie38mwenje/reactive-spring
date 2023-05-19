package com.example.todo.service;

import com.example.todo.config.RabbitMqConfig;
import com.example.todo.domain.Todo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RabbitMqSender {

private final RabbitTemplate rabbitTemplate;
    public RabbitMqSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(Todo todo) {
        rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, "myRoutingKey.messages",
                todo);
        log.info("Sending message to the exchange : {}",todo);
    }
}
