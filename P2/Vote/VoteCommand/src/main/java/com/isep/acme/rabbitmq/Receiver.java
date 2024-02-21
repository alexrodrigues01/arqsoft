package com.isep.acme.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isep.acme.ACMEApplication;
import com.isep.acme.model.Review;
import com.isep.acme.model.dto.ReviewQueueDTO;
import com.isep.acme.model.dto.VoteDTO;
import com.isep.acme.model.dto.VoteQueueDTO;
import com.isep.acme.repositories.ReviewRepository;
import com.isep.acme.repositories.VoteRepository;
import com.isep.acme.services.VoteService;
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

    @Autowired
    private VoteService vService;

    ReviewRepository repository;

    VoteRepository vRepo;

    @Autowired
    public void setVoteRepo(@Value("${vote.repo}") String voteBean, @Value("${review.repo}") String bean, ApplicationContext applicationContext){
        vRepo = (VoteRepository) applicationContext.getBean(voteBean);
        repository = (ReviewRepository) applicationContext.getBean(bean);
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
                    case CREATE -> {
                        if(reviewQueueDTO.getApprovalStatus().equalsIgnoreCase("approved")){
                            repository.save(new Review(reviewQueueDTO.getIdReview()));
                            runner.sendMessage(new EventDTO(TypeOfEvent.CREATE, "Review", new ReviewQueueDTO(reviewQueueDTO.getIdReview())), ACMEApplication.fanoutExchangeName2);
                            VoteQueueDTO v = vService.create(new VoteDTO(reviewQueueDTO.getUserID(),"upVote",reviewQueueDTO.getIdReview()));
                            runner.sendMessage(new EventDTO(TypeOfEvent.CREATE,"Vote", v), ACMEApplication.fanoutExchangeName);
                            runner.sendMessage(new EventDTO(TypeOfEvent.CREATE,"Vote", v), ACMEApplication.fanoutExchangeName2);
                        }
                    }
                    case DELETE -> {
                        Optional<Review> review = repository.findById(reviewQueueDTO.getIdReview());
                        review.ifPresent(value -> repository.delete(value));
                        runner.sendMessage(new EventDTO(TypeOfEvent.DELETE, "Review", new ReviewQueueDTO(reviewQueueDTO.getIdReview())),ACMEApplication.fanoutExchangeName2);
                    }
                    case UPDATE -> {
                        if(reviewQueueDTO.getApprovalStatus().equalsIgnoreCase("approved")){
                            repository.save(new Review(reviewQueueDTO.getIdReview()));
                            runner.sendMessage(new EventDTO(TypeOfEvent.UPDATE, "Review", new ReviewQueueDTO(reviewQueueDTO.getIdReview())),ACMEApplication.fanoutExchangeName2);
                        }
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
