package com.isep.acme.controllers;

import com.isep.acme.model.*;
import com.isep.acme.model.review.ReviewDTO;
import com.isep.acme.model.review.ReviewMapper;
import com.isep.acme.model.user.User;
import com.isep.acme.rabbitmq.Runner;
import com.isep.acme.services.ReviewRecommendation;
import com.isep.acme.services.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findById() throws Exception {
        String sku = "SKU123";
        String status = "approved";
        List<ReviewDTO> expectedReviews = new ArrayList<>();

        when(reviewService.getReviewsOfProduct(eq(sku), eq(status))).thenReturn(expectedReviews);

        ResponseEntity<List<ReviewDTO>> response = reviewController.findById(sku, status);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedReviews, response.getBody());
    }

    @Test
    void findReviewByUser() throws Exception {
        Long userID = 1L;
        List<ReviewDTO> expectedReviews = new ArrayList<>(); // Populate with expected data

        when(reviewService.findReviewsByUser(eq(userID))).thenReturn(expectedReviews);

        ResponseEntity<List<ReviewDTO>> response = reviewController.findReviewByUser(userID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedReviews, response.getBody());
    }

    @Test
    void getPendingReview() throws Exception {
        List<ReviewDTO> expectedPendingReviews = new ArrayList<>();

        when(reviewService.findPendingReview()).thenReturn(expectedPendingReviews);

        ResponseEntity<List<ReviewDTO>> response = reviewController.getPendingReview();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedPendingReviews, response.getBody());
    }

    @Test
    void getRecommendedReviews() {
        Product product= Mockito.mock(Product.class);
        Rating rating= Mockito.mock(Rating.class);
        User user= new User("username", "password");
        user.setUserId(2L);

        //ReviewDTO review = new ReviewDTO(1L,1L,"approved","ReviewText"
         //       , List.of(), List.of(),"Report", Date.valueOf(LocalDate.MAX),"Funfact",product,rating,user);


        when(reviewService.recommendedReviews(1L)).thenReturn(List.of());

        ResponseEntity<List<ReviewDTO>> actual = reviewController.getRecommendedReviews(1L);

        assertEquals(204, actual.getStatusCodeValue());
    }
}