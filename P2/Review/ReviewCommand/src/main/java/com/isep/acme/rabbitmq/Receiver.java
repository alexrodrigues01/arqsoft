package com.isep.acme.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isep.acme.ACMEApplication;
import com.isep.acme.model.EventDTO;
import com.isep.acme.model.Product;
import com.isep.acme.model.Review;
import com.isep.acme.model.TypeOfEvent;
import com.isep.acme.model.product.ProductDTO;
import com.isep.acme.model.vote.Vote;
import com.isep.acme.model.vote.VoteDTO;
import com.isep.acme.repositories.ProductRepository;
import com.isep.acme.repositories.ReviewRepository;
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

    ProductRepository productRepository;

    ReviewRepository repository;

    @Autowired
    public void setProductRepo(@Value("${product.repo}") String productBean,@Value("${review.repo}") String bean, ApplicationContext applicationContext){
        productRepository = (ProductRepository) applicationContext.getBean(productBean);
        repository = (ReviewRepository) applicationContext.getBean(bean);
    }

    private CountDownLatch latch = new CountDownLatch(0);

    public void receiveMessage(String messageJson) throws JsonProcessingException {
        try {
            EventDTO message = objectMapper.readValue(messageJson, EventDTO.class);
            System.out.println("Received <" + message.toString() + ">");
            LinkedHashMap<String, Object> entityMap = (LinkedHashMap<String, Object>) message.getEntity();

            if (message.getDomain().equals("Product")) {
                ProductDTO productDTO = objectMapper.convertValue(entityMap, ProductDTO.class);
                switch (message.getTypeOfEvent()) {
                    case CREATE -> {
                        productRepository.save(new Product(productDTO.getSku()));
                        runner.sendMessage(new EventDTO(TypeOfEvent.CREATE, "Product", new ProductDTO(productDTO.getSku(), productDTO.getDesignation())), ACMEApplication.fanoutExchangeName2);
                    }
                    case DELETE -> {
                        Optional<Product> product = productRepository.findBySku(productDTO.getSku());
                        product.ifPresent(value -> productRepository.delete(value));
                        runner.sendMessage(new EventDTO(TypeOfEvent.DELETE, "Product", new ProductDTO(productDTO.getSku(), productDTO.getDesignation())), ACMEApplication.fanoutExchangeName2);
                    }
                }
            }else{
                VoteDTO voteDTO = objectMapper.convertValue(entityMap, VoteDTO.class);
                switch (message.getTypeOfEvent()) {
                    case CREATE -> {
                        Optional<Review> review = repository.findById(voteDTO.getReviewId());
                        if(voteDTO.getVote().equalsIgnoreCase("upvote")){
                            review.ifPresent(value -> value.addUpVote(new Vote(voteDTO.getUserId())));
                        }else{
                            review.ifPresent(value -> value.addDownVote(new Vote(voteDTO.getUserId())));
                        }
                        runner.sendMessage(message, ACMEApplication.fanoutExchangeName2);
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