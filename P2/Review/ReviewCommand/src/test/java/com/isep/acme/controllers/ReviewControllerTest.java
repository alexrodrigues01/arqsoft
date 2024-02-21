package com.isep.acme.controllers;

import com.isep.acme.model.review.CreateReviewDTO;
import com.isep.acme.model.review.ModerateReviewDTO;
import com.isep.acme.model.review.ReviewDTO;
import com.isep.acme.model.user.User;
import com.isep.acme.rabbitmq.Runner;
import com.isep.acme.services.ReviewRecommendation;
import com.isep.acme.services.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class ReviewControllerTest {

    @InjectMocks
    ReviewController reviewController;

    @Mock
    Runner runner;

    @Mock
    ReviewService reviewService;

    @Mock
    ReviewRecommendation reviewRecommendation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createReview() throws Exception {
        String sku = "SKU123";
        String reviewText = "Review";
        Long userID = 12L;
        Double rating = 5.0;
        LocalDate date = LocalDate.of(2023,2,2);

        CreateReviewDTO createReviewDTO = new CreateReviewDTO(reviewText,userID,rating);
        ReviewDTO expectedReview = new ReviewDTO(1L, "reviewText", Date.valueOf(date),"approved","Engraçado",2.0,10, List.of(new User(1L)));

        when(reviewService.create(eq(createReviewDTO), eq(sku))).thenReturn(expectedReview);

        ResponseEntity<ReviewDTO> response = reviewController.createReview(sku, createReviewDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedReview, response.getBody());
    }

    @Test
    void deleteReview() throws Exception {
        Long reviewID = 1L;

        when(reviewService.deleteReview(eq(reviewID))).thenReturn(true);

        ResponseEntity<Boolean> response = reviewController.deleteReview(reviewID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }

    @Test
    void putAcceptRejectReview() {
        Long reviewID = 1L;
        String approved = "approved";
        LocalDate date = LocalDate.of(2023,2,2);
        ReviewDTO expectedReview = new ReviewDTO(reviewID, "reviewText", Date.valueOf(date),approved,"Engraçado",2.0,10, List.of(new User(1L)));

        when(reviewService.moderateReview(eq(reviewID), eq(approved), eq(1L))).thenReturn(expectedReview);

        ResponseEntity<ReviewDTO> response = reviewController.putAcceptRejectReview(reviewID, new ModerateReviewDTO(1L, approved));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedReview, response.getBody());
    }
}