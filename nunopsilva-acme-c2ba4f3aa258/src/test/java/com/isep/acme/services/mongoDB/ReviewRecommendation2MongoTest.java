package com.isep.acme.services.mongoDB;

import com.isep.acme.model.*;
import com.isep.acme.repositories.mongoDB.ReviewRepositoryMongo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

class ReviewRecommendation2MongoTest {

    @Mock
    private ReviewRepositoryMongo reviewRepository;

    @InjectMocks
    private ReviewRecommendation2Mongo reviewRecommendation2Mongo;

    @Mock
    private ReviewMapperAbstract reviewMapperAbstract;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetReviewsRecommended() {
        List<ReviewMongo> reviews = new ArrayList<>();

        ReviewMongo review1 = createReviewWithVotes(1L, "Review text 1", LocalDate.now(), "Fun Fact 1", 4.5, "upVote", 1L, 2L, 3L);
        ReviewMongo review2 = createReviewWithVotes(2L, "Review text 2", LocalDate.now(), "Fun Fact 2", 3.0, "upVote", 5L, 6L, 7L);
        ReviewMongo review3 = createReviewWithVotes(3L, "Review text 3", LocalDate.now(), "Fun Fact 3", 2.0, "upVote", 1L, 4L);

        reviews.add(review1);
        reviews.add(review2);
        reviews.add(review3);

        Review review4 = createReviewWithVotes2(1L, "Review text 1", LocalDate.now(), "Fun Fact 1", 4.5, "upVote", 1L, 2L, 3L);
        Review review5 = createReviewWithVotes2(2L, "Review text 2", LocalDate.now(), "Fun Fact 2", 3.0, "upVote", 5L, 6L, 7L);
        Review review6 = createReviewWithVotes2(3L, "Review text 3", LocalDate.now(), "Fun Fact 3", 2.0, "upVote", 1L, 4L);


        when(reviewRepository.findAll()).thenReturn(reviews);
        when(reviewMapperAbstract.toReviewListFromMongo(anyList())).thenReturn(List.of(review4, review5, review6));

        List<Review> result = reviewRecommendation2Mongo.getReviewsRecommended(1L);

        assertEquals(1, result.size());
        assertEquals(review5, result.get(0));
    }

    private ReviewMongo createReviewWithVotes(Long id, String reviewText, LocalDate date, String funFact, double rating, String voteType, Long... userIds) {
        ProductMongo product = new ProductMongo("123", "Product Name", "Product Description");
        UserMongo user = new UserMongo("username", "password");
        user.setUserId(1L);

        RatingMongo ratingObj = new RatingMongo(rating);

        ReviewMongo review = new ReviewMongo(id, reviewText, date, product, funFact, ratingObj, user);

        List<Vote> votes = new ArrayList<>();
        for (Long userId : userIds) {
            votes.add(new Vote(voteType, userId));
        }

        if (voteType.equals("upVote")) {
            review.setUpVote(votes);
        } else if (voteType.equals("downVote")) {
            review.setDownVote(votes);
        }

        return review;
    }

    private Review createReviewWithVotes2(Long id, String reviewText, LocalDate date, String funFact, double rating, String voteType, Long... userIds) {
        Product product = new Product("123", "Product Name", "Product Description");
        User user = new User("username", "password");
        user.setUserId(1L);

        Rating ratingObj = new Rating(rating);

        Review review = new Review(id, reviewText, date, product, funFact, ratingObj, user);

        List<Vote> votes = new ArrayList<>();
        for (Long userId : userIds) {
            votes.add(new Vote(voteType, userId));
        }

        if (voteType.equals("upVote")) {
            review.setUpVote(votes);
        } else if (voteType.equals("downVote")) {
            review.setDownVote(votes);
        }

        return review;
    }
}
