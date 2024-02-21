package com.isep.acme.services;

import com.isep.acme.model.*;
import com.isep.acme.repositories.h2.ReviewRepository;
import com.isep.acme.services.h2.ReviewRecommendation1;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ReviewRecommendation1Test {
    ReviewRecommendation1 reviewRecommendation1;

     Review review;

    @BeforeEach
    void setUp(){
        ReviewRepository reviewRepository= Mockito.mock(ReviewRepository.class);
        reviewRecommendation1= new ReviewRecommendation1(reviewRepository);

        Product product= Mockito.mock(Product.class);
        Rating rating= Mockito.mock(Rating.class);
        User user= Mockito.mock(User.class);
        review= new Review(1L,1L,"approved","ReviewText"
                ,List.of(), List.of(),"Report", LocalDate.MAX,"Funfact",product,rating,user);

        List<Vote> upvotes= new ArrayList<>();
        upvotes.add(new Vote("vote1",2L));
        upvotes.add(new Vote("vote2",3L));
        upvotes.add(new Vote("vote3",4L));
        upvotes.add(new Vote("vote4",5L));
        upvotes.add(new Vote("vote5",6L));

        List<Vote> downvotes= new ArrayList<>();
        downvotes.add(new Vote("downVote1",7L));
        downvotes.add(new Vote("downvote2",8L));

        review.setUpVote(upvotes);
        review.setDownVote(downvotes);


        Mockito.when(reviewRepository.findAll()).thenReturn(List.of(review));
    }

    @Test
    void testGetReviewsRecommended(){
        List<Review> reviews= reviewRecommendation1.getReviewsRecommended(1L);
        assertEquals(List.of(review),reviews);
    }

    @Test
    void testGetNoReviewsRecommended(){
        List<Vote> downvotes= new ArrayList<>();
        downvotes.add(new Vote("downVote1",7L));
        downvotes.add(new Vote("downvote2",8L));
        downvotes.add(new Vote("downvote2",9L));
        downvotes.add(new Vote("downvote2",10L));
        downvotes.add(new Vote("downvote2",11L));
        review.setDownVote(downvotes);
        List<Review> reviews= reviewRecommendation1.getReviewsRecommended(1L);
        assertEquals(List.of(),reviews);
    }
}
