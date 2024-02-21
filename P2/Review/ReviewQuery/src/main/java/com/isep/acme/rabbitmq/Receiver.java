package com.isep.acme.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isep.acme.model.*;
import com.isep.acme.model.product.ProductDTO;
import com.isep.acme.model.review.AcceptRejectReviewDTO;
import com.isep.acme.model.review.ReviewQueueDTO;
import com.isep.acme.model.vote.Vote;
import com.isep.acme.model.vote.VoteDTO;
import com.isep.acme.repositories.ProductRepository;
import com.isep.acme.repositories.ReviewRepository;
import com.isep.acme.services.ProductServiceImpl;
import com.isep.acme.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

@Component
public class Receiver {
    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Runner runner;
    private CountDownLatch latch = new CountDownLatch(0);

    ReviewRepository repository;

    @Autowired
    public void setProductRepo(@Value("${product.repo}") String productBean, @Value("${review.repo}") String bean, ApplicationContext applicationContext){
        repository = (ReviewRepository) applicationContext.getBean(bean);
    }

    public void receiveMessage(String messageJson) throws JsonProcessingException {
        EventDTO message =  objectMapper.readValue(messageJson, EventDTO.class);
        System.out.println("Received <" + message.toString() + ">");
        LinkedHashMap<String, Object> entityMap = (LinkedHashMap<String, Object>) message.getEntity();
        try {
            if (message.getDomain().equals("Review")) {
                switch (message.getTypeOfEvent()) {
                    case CREATE -> {
                        ReviewQueueDTO reviewDTO = objectMapper.convertValue(entityMap, ReviewQueueDTO.class);
                        reviewService.create(reviewDTO);
                    }
                    case DELETE -> {
                        ReviewQueueDTO reviewDTO = objectMapper.convertValue(entityMap, ReviewQueueDTO.class);
                        reviewService.delete(reviewDTO.getIdReview());
                    }
                    case UPDATE -> {
                        ReviewQueueDTO acceptRejectReviewDTO = objectMapper.convertValue(entityMap, ReviewQueueDTO.class);
                        reviewService.updateReview(acceptRejectReviewDTO);
                    }
                }
            } else if(message.getDomain().equals("Product")){
                ProductDTO productDTO = objectMapper.convertValue(entityMap, ProductDTO.class);
                switch (message.getTypeOfEvent()) {
                    case CREATE -> {
                        productService.create(new Product(productDTO.getSku()));
//                        runner.sendMessage(new EventDTO(TypeOfEvent.CREATE,"Product", new ProductDTO(productDTO.getSku(), productDTO.getDesignation())));
                    }
                    case DELETE -> {
                        productService.deleteBySku(productDTO.getSku());
//                        runner.sendMessage(new EventDTO(TypeOfEvent.DELETE,"Product", new ProductDTO( productDTO.getSku(), productDTO.getDesignation())));
                    }
                }
            } else {
                VoteDTO voteDTO = objectMapper.convertValue(entityMap, VoteDTO.class);
                switch (message.getTypeOfEvent()) {
                    case CREATE -> {
                        Optional<Review> review = repository.findById(voteDTO.getReviewId());
                        if(voteDTO.getVote().equalsIgnoreCase("upvote")){
                            review.ifPresent(value -> value.addUpVote(new Vote(voteDTO.getUserId())));
                        }else{
                            review.ifPresent(value -> value.addDownVote(new Vote(voteDTO.getUserId())));
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        latch.countDown();
    }


    public CountDownLatch getLatch() {
        return latch;
    }

}