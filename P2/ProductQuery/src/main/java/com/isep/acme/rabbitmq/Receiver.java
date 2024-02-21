package com.isep.acme.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isep.acme.model.Product;
import com.isep.acme.model.dto.ProductDTO;
import com.isep.acme.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.concurrent.CountDownLatch;

@Component
public class Receiver {

    @Autowired
    private ProductService service;
    private CountDownLatch latch = new CountDownLatch(0);

    @Autowired
    private ObjectMapper objectMapper;

    public void receiveMessage(String messageJson) throws JsonProcessingException {
        try {
            EventDTO message =  objectMapper.readValue(messageJson, EventDTO.class);
            System.out.println("Received boda <" + message.toString() + ">");
            LinkedHashMap<String, Object> entityMap = (LinkedHashMap<String, Object>) message.getEntity();
            ProductDTO productDTO= objectMapper.convertValue(entityMap, ProductDTO.class);
            switch (message.getTypeOfEvent()) {
                case CREATE -> service.create(new Product(productDTO.getSku(), productDTO.getDesignation(), productDTO.getDesignation()));
                case DELETE -> service.deleteBySku(productDTO.getSku());
                case UPDATE -> service.updateBySku(productDTO.getSku(), new Product(productDTO.getSku(), productDTO.getDesignation(), productDTO.getDesignation()));
            }

            latch.countDown();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    public CountDownLatch getLatch() {
        return latch;
    }

}