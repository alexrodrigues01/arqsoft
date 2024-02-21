package com.isep.acme.services.acme.services;

import com.isep.acme.model.*;
import com.isep.acme.repositories.ReviewRepository;
import com.isep.acme.services.recommendationReviews.ReviewRecommendation2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
class ReviewRecommendation2Test {
    @InjectMocks
    private ReviewRecommendation2 reviewRecommendation2;

    @Mock
    ReviewRepository repository;

    Review review;
    Review review3;
    Review review4;

    @BeforeEach
    void setUp(){

        Product product= Mockito.mock(Product.class);
        Rating rating= Mockito.mock(Rating.class);
        User user= new User("username", "password");
        user.setUserId(2L);

        review = new Review(1L,1L,"approved","ReviewText"
                ,List.of(), List.of(),"Report", Date.valueOf(LocalDate.MAX),"Funfact",product,rating,user);
        Vote vote1 = new Vote("vote1",1L);
        Vote vote2 = new Vote("vote2",2L);
        Vote vote3 = new Vote("vote3",3L);


        Vote vote7 = new Vote("downVote1",7L);
        review.setUpVote(List.of(vote1, vote2, vote3));
        review.setDownVote(List.of(vote7));

        review3 = new Review(3L,1L,"approved","ReviewText"
                ,List.of(), List.of(),"Report", Date.valueOf(LocalDate.MAX),"Funfact",product,rating,user);
        review3.setUpVote(List.of(vote3, vote2));
        review3.setDownVote(List.of());

        review4 = new Review(4L,1L,"approved","ReviewText"
                ,List.of(), List.of(),"Report", Date.valueOf(LocalDate.MAX),"Funfact",product,rating,user);
        review4.setUpVote(List.of(vote3, vote2));
        review4.setDownVote(List.of());

        Mockito.when(repository.findAll()).thenReturn(List.of(review, review3, review4));
    }

    @Test
    void testGetReviewsRecommendedEmpty(){
        List<Review> reviews= reviewRecommendation2.getReviewsRecommended(1L);
        assertEquals(List.of(),reviews);
    }

    @Test
    void testGetReviewsRecommended(){
        Vote vote1 = new Vote("vote1",1L);
        Vote vote2 = new Vote("vote2",2L);
        Vote vote3 = new Vote("vote3",3L);
        review3.setUpVote(List.of(vote1, vote3, vote2));
        List<Review> reviews= reviewRecommendation2.getReviewsRecommended(1L);
        assertEquals(List.of(review4),reviews);
    }

}