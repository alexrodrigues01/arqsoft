package com.isep.acme.services.acme.services;

import com.isep.acme.model.*;
import com.isep.acme.repositories.ReviewRepository;
import com.isep.acme.services.recommendationReviews.ReviewRecommendation1;
import com.isep.acme.services.recommendationReviews.ReviewRecommendation2;
import com.isep.acme.services.recommendationReviews.ReviewRecommendation3;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ReviewRecommendation3Test {

    @Mock
    private ReviewRecommendation1 reviewRecommendation1;

    @Mock
    private ReviewRecommendation2 reviewRecommendation2;

    @InjectMocks
    private ReviewRecommendation3 reviewRecommendation3;

    @Mock
    private ReviewRepository repository;

    private Review review;
    private Review review3;
    private Review review4;


    @BeforeEach
    void setUp(){
        Product product= Mockito.mock(Product.class);
        Rating rating= new Rating(1L,1L,2.0);
        User user= new User("username", "password");
        user.setUserId(2L);


        Rating rating2= new Rating(2L,1L,3.0);
        review = new Review(1L,1L,"approved","ReviewText"
                ,List.of(), List.of(),"Report", Date.valueOf(LocalDate.MAX),"Funfact",product,rating2,user);
        Vote vote1 = new Vote("vote1",1L);
        Vote vote2 = new Vote("vote2",2L);
        Vote vote3 = new Vote("vote3",3L);


        Vote vote7 = new Vote("downVote1",7L);
        review.setUpVote(List.of(vote1, vote2, vote3));
        review.setDownVote(List.of(vote7));


        review3 = new Review(3L,1L,"approved","ReviewText"
                ,List.of(), List.of(),"Report", Date.valueOf(LocalDate.MAX),"Funfact",product,rating2,user);
        review3.setUpVote(List.of(vote3, vote2));
        review3.setDownVote(List.of());

        User user2= new User("username", "password");
        user2.setUserId(1L);
        review4 = new Review(4L,1L,"approved","ReviewText"
                ,List.of(), List.of(),"Report", Date.valueOf(LocalDate.MAX),"Funfact",product,rating,user2);
        review4.setUpVote(List.of(vote3, vote2));
        review4.setDownVote(List.of());

        when(repository.findByUserId(any(User.class))).thenReturn(Optional.of(List.of(review,review3)));
        when(repository.findReviewVotedByUserId(anyLong())).thenReturn(Optional.of(List.of(review4)));


        when(reviewRecommendation1.getReviewsRecommended(any(Long.class))).thenReturn(List.of(review,review3));
        when(reviewRecommendation2.getReviewsRecommended(any(Long.class))).thenReturn(List.of(review3));

    }

    @Test
    void testReviewsRecommended(){
        Vote vote1 = new Vote("vote1",1L);
        Vote vote2 = new Vote("vote2",2L);
        Vote vote3 = new Vote("vote3",3L);
        review3.setUpVote(List.of(vote1, vote3, vote2));
        Rating rating= new Rating(1L,1L,2.0);
        review3.setRating(rating);
        review.setRating(rating);
        List<Review> reviewsRecomendadas= reviewRecommendation3.getReviewsRecommended(1L);
        assertEquals(List.of(review3),reviewsRecomendadas);
    }

    @Test
    void testNoReviewsRecommended(){
        Vote vote1 = new Vote("vote1",1L);
        Vote vote2 = new Vote("vote2",2L);
        Vote vote3 = new Vote("vote3",3L);
        review3.setUpVote(List.of(vote1, vote3, vote2));
        Rating rating= Mockito.mock(Rating.class);
        rating.setRate(3.0);
        review3.setRating(rating);

        List<Review> reviewsRecomendadas= reviewRecommendation3.getReviewsRecommended(1L);
        assertEquals(List.of(),reviewsRecomendadas);
    }
}
