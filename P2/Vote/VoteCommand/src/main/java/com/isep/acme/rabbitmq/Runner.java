package com.isep.acme.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.isep.acme.ACMEApplication;

@Component
public class Runner {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public Runner( RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    public void sendMessage(EventDTO eventDTO, String fanOut) throws JsonProcessingException {
        System.out.println("Sending message...");
        System.out.println(objectMapper.writeValueAsString(eventDTO));
        rabbitTemplate.convertAndSend(fanOut,"",objectMapper.writeValueAsString(eventDTO));
    }

}
