package com.example.todo.service;

import com.example.todo.config.RabbitMqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProduceMessageService {
    private final RabbitTemplate rabbitTemplate;

    public ProduceMessageService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public String produceMessage(String message) {
        rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, "myRoutingKey.messages",
                message);
        log.info("Sending message to the exchange : {}",message);
        return "Message(" + message + ")" + " has been produced.";
    }
}
