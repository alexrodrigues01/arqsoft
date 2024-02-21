package com.isep.acme.services.acme.controllers;

import com.isep.acme.controllers.ReviewController;
import com.isep.acme.model.*;
import com.isep.acme.model.dto.CreateReviewDTO;
import com.isep.acme.model.dto.ReviewDTO;
import com.isep.acme.model.dto.VoteReviewDTO;
import com.isep.acme.services.ReviewRecommendation;
import com.isep.acme.services.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class ReviewControllerTest {

    @InjectMocks
    ReviewController reviewController;

    @Mock
    ReviewService reviewService;

    @Mock
    ReviewRecommendation reviewRecommendation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findById() {
        String sku = "SKU123";
        String status = "approved";
        List<ReviewDTO> expectedReviews = new ArrayList<>(); // Populate with expected data

        when(reviewService.getReviewsOfProduct(eq(sku), eq(status))).thenReturn(expectedReviews);

        ResponseEntity<List<ReviewDTO>> response = reviewController.findById(sku, status);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedReviews, response.getBody());
    }

    @Test
    void createReview() {
        String sku = "SKU123";
        String reviewText = "Review";
        Long userID = 12L;
        Double rating = 5.0;
        LocalDate date = LocalDate.of(2023,2,2);

        CreateReviewDTO createReviewDTO = new CreateReviewDTO(reviewText,userID,rating);
        ReviewDTO expectedReview = new ReviewDTO(5L, reviewText, Date.valueOf(date),"Aprovado","Engraçado",rating,10);

        when(reviewService.create(eq(createReviewDTO), eq(sku))).thenReturn(expectedReview);

        ResponseEntity<ReviewDTO> response = reviewController.createReview(sku, createReviewDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedReview, response.getBody());
    }

    @Test
    void addVote() {
        Long reviewID = 1L;
        VoteReviewDTO voteReviewDTO = new VoteReviewDTO(10L,"Voto"); // Populate with test data

        when(reviewService.addVoteToReview(eq(reviewID), eq(voteReviewDTO))).thenReturn(true);

        ResponseEntity<Boolean> response = reviewController.addVote(reviewID, voteReviewDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody());
    }

    @Test
    void findReviewByUser() {
        Long userID = 1L;
        List<ReviewDTO> expectedReviews = new ArrayList<>(); // Populate with expected data

        when(reviewService.findReviewsByUser(eq(userID))).thenReturn(expectedReviews);

        ResponseEntity<List<ReviewDTO>> response = reviewController.findReviewByUser(userID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedReviews, response.getBody());
    }

    @Test
    void deleteReview() {
        Long reviewID = 1L;

        when(reviewService.DeleteReview(eq(reviewID))).thenReturn(true);

        ResponseEntity<Boolean> response = reviewController.deleteReview(reviewID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }

    @Test
    void putAcceptRejectReview() {
        Long reviewID = 1L;
        String approved = "approved";
        LocalDate date = LocalDate.of(2023,2,2);
        ReviewDTO expectedReview = new ReviewDTO(reviewID, "reviewText", Date.valueOf(date),approved,"Engraçado",2.0,10);

        when(reviewService.moderateReview(eq(reviewID), eq(approved))).thenReturn(expectedReview);

        ResponseEntity<ReviewDTO> response = reviewController.putAcceptRejectReview(reviewID, approved);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedReview, response.getBody());
    }

    @Test
    void getPendingReview() {
        List<ReviewDTO> expectedPendingReviews = new ArrayList<>();

        when(reviewService.findPendingReview()).thenReturn(expectedPendingReviews);

        ResponseEntity<List<ReviewDTO>> response = reviewController.getPendingReview();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedPendingReviews, response.getBody());
    }

    @Test
    void getRecomendedReviews() {
        Product product= Mockito.mock(Product.class);
        Rating rating= Mockito.mock(Rating.class);
        User user= new User("username", "password");
        user.setUserId(2L);

        ReviewDTO expectedReview = new ReviewDTO(5L, "reviewText", Date.valueOf(LocalDate.MAX),"Aprovado","Engraçado",4.0,10);

        when(reviewService.recommendedReviews(1L)).thenReturn(List.of(expectedReview));

        ResponseEntity<List<ReviewDTO>> actual = reviewController.getRecommendedReviews(1L);

        assertEquals(200, actual.getStatusCodeValue());
        assertEquals(List.of(expectedReview), actual.getBody());
    }
}