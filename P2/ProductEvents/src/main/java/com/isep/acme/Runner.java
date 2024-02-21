package com.isep.acme;

import java.util.concurrent.TimeUnit;


import com.isep.acme.rabbitmq.Receiver;
import com.isep.acme.utils.EventDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class Runner  {

    private final RabbitTemplate rabbitTemplate;
    private final Receiver receiver;

    public Runner(Receiver receiver, RabbitTemplate rabbitTemplate) {
        this.receiver = receiver;
        this.rabbitTemplate = rabbitTemplate;
    }


    public void sendMessage(EventDTO eventDTO) throws Exception {
        System.out.println("Sending message...");
        rabbitTemplate.convertAndSend(ACMEApplication.fanoutExchangeName,  eventDTO);
        receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
    }

}
