//package com.isep.acme;
//
//
//import com.isep.acme.model.review.CreateReviewDTO;
//import com.isep.acme.model.review.ReviewDTO;
//import com.isep.acme.repositories.mongo.MongoProductRepositoryImp;
//import com.isep.acme.repositories.mongo.MongoReviewRepositoryImp;
//import com.isep.acme.repositories.mongo.MongoUserRepositoryImp;
//import com.isep.acme.services.*;
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
//    private ReviewServiceImpl reviewService;
//
//    @Autowired
//    private MongoProductRepositoryImp productRepositoryImp;
//
//    @Autowired
//    private MongoReviewRepositoryImp mongoReviewRepositoryImp;
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private RatingService ratingService;
//
//    @Autowired
//    private RestService restService;
//
//    @Autowired
//    private ReviewRecommendation4 reviewRecommendation;
//
//    @BeforeEach
//    void setUp(){
//        reviewService = new ReviewServiceImpl();
//        reviewService.setpRepository(productRepositoryImp);
//        reviewService.setRepository(mongoReviewRepositoryImp);
//        reviewService.setRatingService(ratingService);
//        reviewService.setUserService(userService);
//        reviewService.setRestService(restService);
//        reviewService.setReviewRecommendation(reviewRecommendation);
//    }
//
//    @Test
//    public void testApproveReview() {
//        String sku= "vgb576hgb675";
//        ReviewDTO review = reviewService.create(new CreateReviewDTO("review", 1L, 5.0), sku);
//
//        ReviewDTO approved1 = reviewService.moderateReview(review.getIdReview(), "approved", 2L);
//        Assertions.assertEquals(approved1.getApprovalStatus(), "pending");
//        Assertions.assertEquals(approved1.getApproves().size(), 1);
//
//
//        ReviewDTO approved2 = reviewService.moderateReview(review.getIdReview(), "approved", 3L);
//        Assertions.assertEquals(approved2.getApprovalStatus(), "approved");
//        Assertions.assertEquals(approved2.getApproves().size(), 2);
//
//        reviewService.deleteReview(review.getIdReview());
//    }
//}
