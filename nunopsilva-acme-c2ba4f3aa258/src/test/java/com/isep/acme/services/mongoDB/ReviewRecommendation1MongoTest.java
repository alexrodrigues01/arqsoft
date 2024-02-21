package com.isep.acme.services.mongoDB;

import com.isep.acme.model.*;
import com.isep.acme.repositories.mongoDB.ReviewRepositoryMongo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class ReviewRecommendation1MongoTest {

    @Mock
    private ReviewRepositoryMongo reviewRepository;

    @InjectMocks
    private ReviewRecommendation1Mongo reviewRecommendation1Mongo;

    @Mock
    private ReviewMapperAbstract reviewMapperAbstract;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetReviewsRecommended() {
        ProductMongo product = new ProductMongo("123", "Product Name", "Product Description");
        RatingMongo rating = new RatingMongo(4.5);
        UserMongo user = new UserMongo("username", "password");

        ReviewMongo review1 = new ReviewMongo(1L, "Review text 1", LocalDate.now(), product, "Fun Fact 1", rating, user);
        List<Vote> upVotes1 = new ArrayList<>();
        upVotes1.add(new Vote("upVote", 1L));
        upVotes1.add(new Vote("upVote", 2L));
        upVotes1.add(new Vote("upVote", 3L));
        upVotes1.add(new Vote("upVote", 4L));
        review1.setUpVote(upVotes1);

        ReviewMongo review2 = new ReviewMongo(1L, "Review text 2", LocalDate.now(), product, "Fun Fact 2", rating, user);
        List<Vote> upVotes2 = new ArrayList<>();
        upVotes2.add(new Vote("upVote", 5L));
        upVotes2.add(new Vote("upVote", 6L));
        upVotes2.add(new Vote("upVote", 7L));
        review2.setUpVote(upVotes2);

        ReviewMongo review3 = new ReviewMongo(1L, "Review text 3", LocalDate.now(), product, "Fun Fact 3", rating, user);
        List<Vote> upVotes3 = new ArrayList<>();
        upVotes3.add(new Vote("upVote", 8L));
        upVotes3.add(new Vote("upVote", 9L));
        upVotes3.add(new Vote("upVote", 10L));
        upVotes3.add(new Vote("upVote", 11L));
        upVotes3.add(new Vote("upVote", 12L));
        review3.setUpVote(upVotes3);

        List<ReviewMongo> reviews = List.of(review1, review2, review3);

        when(reviewRepository.findAll()).thenReturn(reviews);
        when(reviewMapperAbstract.toReviewListFromMongo(anyList())).thenReturn(createReviews());

        List<Review> result = reviewRecommendation1Mongo.getReviewsRecommended(1L);
        //TODO MUDAR TIPO
        assertEquals(2, result.size());
    }

    private List<Review> createReviews(){
        Product product = new Product("123", "Product Name", "Product Description");
        Rating rating = new Rating(4.5);
        User user = new User("username", "password");

        Review review1 = new Review(1L, "Review text 1", LocalDate.now(), product, "Fun Fact 1", rating, user);
        List<Vote> upVotes1 = new ArrayList<>();
        upVotes1.add(new Vote("upVote", 1L));
        upVotes1.add(new Vote("upVote", 2L));
        upVotes1.add(new Vote("upVote", 3L));
        upVotes1.add(new Vote("upVote", 4L));
        review1.setUpVote(upVotes1);

        Review review3 = new Review(1L, "Review text 3", LocalDate.now(), product, "Fun Fact 3", rating, user);
        List<Vote> upVotes3 = new ArrayList<>();
        upVotes3.add(new Vote("upVote", 8L));
        upVotes3.add(new Vote("upVote", 9L));
        upVotes3.add(new Vote("upVote", 10L));
        upVotes3.add(new Vote("upVote", 11L));
        upVotes3.add(new Vote("upVote", 12L));
        review3.setUpVote(upVotes3);

        return List.of(review1, review3);
    }
}
