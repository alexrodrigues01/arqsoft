//package com.isep.acme;
//import com.isep.acme.model.*;
//import com.isep.acme.model.dto.VoteDTO;
//import com.isep.acme.model.dto.VoteQueueDTO;
//import com.isep.acme.repositories.mongoDB.ReviewRepositoryMongo;
//import com.isep.acme.repositories.mongoDB.VoteRepositoryMongo;
//import com.isep.acme.services.UserServiceImpl;
//import com.isep.acme.services.VoteServiceImpl;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//
//@SpringBootTest
//public class IntegrationTest {
//
//    private VoteServiceImpl voteService;
//
//    @Autowired
//    private UserServiceImpl userService;
//
//    @Autowired
//    private VoteRepositoryMongo voteRepositoryImp;
//
//    @Autowired
//    private ReviewRepositoryMongo reviewRepositoryImp;
//
//
//    @BeforeEach
//    void setUp(){
//        voteService = new VoteServiceImpl();
//        voteService.setVoteRepo(voteRepositoryImp);
//        voteService.setVoteReviewRepo(reviewRepositoryImp);
//        voteService.setVoteUserService(userService);
//    }
//
//    @Test
//    public void testCreateVote() {
//        Long userId = 1L;
//        String vote = "upVote";
//        Long reviewId = 10L;
//        Review review = new Review(10L);
//        reviewRepositoryImp.save(review);
//        VoteQueueDTO v = voteService.create(new VoteDTO(userId, vote,reviewId));
//
//        Assertions.assertEquals(v.getVote(),vote);
//        Assertions.assertEquals(v.getUserId(),userId);
//        Assertions.assertEquals(v.getReviewId(),reviewId);
//
//        voteRepositoryImp.deleteById(v.getVoteId());
//    }
//}
