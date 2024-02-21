package com.isep.acme.rabbitmq;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.isep.acme.ACMEApplication;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class Runner  {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public Runner( RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    public void sendMessage(EventDTO eventDTO) throws JsonProcessingException {
        System.out.println("Sending message...");
        rabbitTemplate.convertAndSend(ACMEApplication.fanoutExchangeName,"",objectMapper.writeValueAsString(eventDTO));
    }

}
