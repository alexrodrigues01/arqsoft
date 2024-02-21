package com.isep.acme.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isep.acme.model.Review;
import com.isep.acme.model.Vote;
import com.isep.acme.model.dto.ReviewQueueDTO;
import com.isep.acme.model.dto.VoteQueueDTO;
import com.isep.acme.repositories.ReviewRepository;
import com.isep.acme.repositories.VoteRepository;
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
    private ObjectMapper objectMapper;

    @Autowired
    private Runner runner;

    ReviewRepository rRepo;

    VoteRepository vRepo;

    @Autowired
    public void setVoteRepo(@Value("${vote.repo}") String voteBean, @Value("${review.repo}") String bean, ApplicationContext applicationContext){
        vRepo = (VoteRepository) applicationContext.getBean(voteBean);
        rRepo = (ReviewRepository) applicationContext.getBean(bean);
    }

    private CountDownLatch latch = new CountDownLatch(0);

    public void receiveMessage(String messageJson) throws JsonProcessingException {
        try {
            EventDTO message = objectMapper.readValue(messageJson, EventDTO.class);
            System.out.println("Received <" + message.toString() + ">");
            LinkedHashMap<String, Object> entityMap = (LinkedHashMap<String, Object>) message.getEntity();

            if (message.getDomain().equals("Review")) {
                ReviewQueueDTO reviewQueueDTO = objectMapper.convertValue(entityMap, ReviewQueueDTO.class);
                switch (message.getTypeOfEvent()) {
                    case CREATE, UPDATE -> {
                            rRepo.save(new Review(reviewQueueDTO.getIdReview()));
                    }
                    case DELETE -> {
                        Optional<Review> review = rRepo.findById(reviewQueueDTO.getIdReview());
                        review.ifPresent(value -> rRepo.delete(value));
                    }
                }
            }else if(message.getDomain().equals("Vote")){
                VoteQueueDTO voteQueueDTO = objectMapper.convertValue(entityMap, VoteQueueDTO.class);
                switch (message.getTypeOfEvent()) {
                    case CREATE -> {
                        vRepo.save(new Vote(voteQueueDTO.getVoteId(),voteQueueDTO.getUserId(),voteQueueDTO.getVote(),voteQueueDTO.getReviewId()));
                    }
                }
            }
        }catch (Exception e){
            System.out.println(e);
        }
        latch.countDown();
    }


    public CountDownLatch getLatch() {
        return latch;
    }
}
