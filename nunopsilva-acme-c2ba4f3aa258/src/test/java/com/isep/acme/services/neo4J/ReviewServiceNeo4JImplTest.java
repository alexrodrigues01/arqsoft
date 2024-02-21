package com.isep.acme.services.neo4J;

import com.isep.acme.controllers.ResourceNotFoundException;
import com.isep.acme.model.*;
import com.isep.acme.model.dto.CreateReviewDTO;
import com.isep.acme.model.dto.ReviewDTO;
import com.isep.acme.model.dto.VoteReviewDTO;
import com.isep.acme.repositories.neo4J.ProductRepositoryNeo4j;
import com.isep.acme.repositories.neo4J.ReviewRepositoryNeo4J;
import com.isep.acme.repositories.neo4J.UserRepositoryNeo4J;
import com.isep.acme.services.RestService;
import com.isep.acme.services.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ReviewServiceNeo4JImplTest {

    @Mock
    private ReviewRepositoryNeo4J repository;

    @Mock
    private ProductRepositoryNeo4j pRepository;

    @Mock
    private UserRepositoryNeo4J uRepository;

    @Mock
    private UserServiceNeo4JImpl userService;

    @Mock
    private RatingServiceNeo4JImpl ratingService;

    @Mock
    private RestService restService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private RatingMapper ratingMapper;

    @InjectMocks
    private ReviewServiceNeo4JImpl reviewService;

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void testGetAll() {
        List<ReviewNeo4J> reviews = new ArrayList<>();
        when(repository.findAll()).thenReturn(reviews);

        Iterable<Review> result = reviewService.getAll();

        verify(repository).findAll();
        assertEquals(reviews, result);
    }

    @Test
    void testCreate() {
        String sku = "123";
        CreateReviewDTO createReviewDTO = new CreateReviewDTO("ReviewText", 1L, 4.5);
        LocalDate date = LocalDate.of(2023, 1, 1);
        Rating rating = new Rating(4.5);
        ProductNeo4J product = new ProductNeo4J(1L, sku, "Test Product", "Product Description");
        User user = new User("username", "password");
        user.setUserId(1L);
        ProductNeo4J productNeo4J = new ProductNeo4J(1L, sku, "Test Product", "Description");

        when(pRepository.findBySku(sku)).thenReturn(Optional.of(productNeo4J));
        when(userService.getUserId(1L)).thenReturn(Optional.of(user));
        when(ratingService.findByRate(4.5)).thenReturn(Optional.of(rating));
        when(restService.getFunFact(any())).thenReturn("Fun Fact");

        ReviewNeo4J review = new ReviewNeo4J("Review 2", LocalDate.now(), product, "Fun Fact 2", new RatingNeo4J(3.0), new UserNeo4J("username", "password"));
        when(repository.save(review)).thenReturn(review);

        ReviewDTO result = reviewService.create(createReviewDTO, sku);

        verify(pRepository).findBySku(sku);
        verify(userService).getUserId(1L);
        verify(ratingService).findByRate(4.5);
        verify(repository).save(any());
    }

    @Test
    void testGetReviewsOfProduct() {
        String sku = "123";
        ProductNeo4J product = new ProductNeo4J(1L, sku, "Test Product", "Product Description");
        when(pRepository.findBySku(sku)).thenReturn(Optional.of(product));

        List<ReviewNeo4J> reviews = new ArrayList<>();
        when(repository.findByProductIdStatus(product.getId(), "Approved")).thenReturn(Optional.of(reviews));

        List<ReviewDTO> result = reviewService.getReviewsOfProduct(sku, "Approved");

        verify(pRepository).findBySku(sku);
        verify(repository).findByProductIdStatus(product.getId(), "Approved");
        assertEquals(reviews, result);
    }

    @Test
    void testAddVoteToReview() {
        Long reviewID = 1L;
        String sku = "123";
        ProductNeo4J product = new ProductNeo4J(1L, sku, "Test Product", "Product Description");
        ReviewNeo4J review = new ReviewNeo4J("Review 2", LocalDate.now(), product, "Fun Fact 2", new RatingNeo4J(3.0), new UserNeo4J("username", "password"));
        VoteReviewDTO voteReviewDTO = new VoteReviewDTO(1L, "upvote");

        when(repository.findById(reviewID)).thenReturn(Optional.of(review));
        when(repository.save(review)).thenReturn(review);

        boolean result = reviewService.addVoteToReview(reviewID, voteReviewDTO);

        verify(repository).findById(reviewID);
        assertFalse(result);
    }

    @Test
    void testGetWeightedAverage() {
        ProductNeo4J product = new ProductNeo4J(1L, "123", "Product Name", "Product Description");
        Product product1 = new Product(1L, "123", "Product Name", "Product Description");
        List<ReviewNeo4J> reviews = new ArrayList<>();
        reviews.add(new ReviewNeo4J("Review 1", LocalDate.now(), product, "Fun Fact 1", new RatingNeo4J(4.5), new UserNeo4J("username", "password")));
        reviews.add(new ReviewNeo4J("Review 2", LocalDate.now(), product, "Fun Fact 2", new RatingNeo4J(3.0), new UserNeo4J("username", "password")));
        when(repository.findByProductId(product.getId())).thenReturn(Optional.of(reviews));

        double result = reviewService.getWeightedAverage(product1);

        verify(repository).findByProductId(product.getId());
        assertEquals(3.75, result);
    }

    @Test
    void testDeleteReview() {
        Long reviewId = 1L;
        String sku = "123";
        ProductNeo4J product = new ProductNeo4J(1L, sku, "Test Product", "Product Description");
        ReviewNeo4J review = new ReviewNeo4J("Review 2", LocalDate.now(), product, "Fun Fact 2", new RatingNeo4J(3.0), new UserNeo4J("username", "password"));
        when(repository.findById(reviewId)).thenReturn(Optional.of(review));

        boolean result = reviewService.DeleteReview(reviewId);

        verify(repository).findById(reviewId);
        verify(repository).delete(review);
        assertTrue(result);
    }

    @Test
    void testFindPendingReview() {
        List<ReviewNeo4J> reviews = new ArrayList<>();
        when(repository.findPendingReviews()).thenReturn(Optional.of(reviews));

        List<ReviewDTO> result = reviewService.findPendingReview();

        verify(repository).findPendingReviews();
        assertEquals(reviews, result);
    }

    @Test
    void testModerateReview() {
        Long reviewID = 1L;
        String sku = "123";
        ProductNeo4J product = new ProductNeo4J(1L, sku, "Test Product", "Product Description");
        ReviewNeo4J review = new ReviewNeo4J("Review 2", LocalDate.now(), product, "Fun Fact 2", new RatingNeo4J(3.0), new UserNeo4J("username", "password"));
        when(repository.findById(reviewID)).thenReturn(Optional.of(review));
        when(repository.save(review)).thenReturn(review);

        assertThrows(ResourceNotFoundException.class, () -> reviewService.moderateReview(2L, "Approved"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> reviewService.moderateReview(reviewID, "InvalidStatus"));
        assertEquals("Invalid status value", exception.getMessage());

        ReviewDTO result = reviewService.moderateReview(reviewID, "Approved");

        verify(repository, times(2)).findById(reviewID);
        verify(repository).save(review);
        assertNotNull(result);
    }

    @Test
    void testFindReviewsByUser() {
        Long userID = 1L;
        UserNeo4J user = new UserNeo4J("username", "password");
        when(uRepository.findById(userID)).thenReturn(Optional.of(user));

        List<ReviewNeo4J> reviews = new ArrayList<>();
        when(repository.findByUserId(user.getUserId())).thenReturn(Optional.of(reviews));

        List<ReviewDTO> result = reviewService.findReviewsByUser(userID);

        verify(uRepository).findById(userID);
        verify(repository).findByUserId(user.getUserId());
        assertEquals(reviews, result);
    }
}
