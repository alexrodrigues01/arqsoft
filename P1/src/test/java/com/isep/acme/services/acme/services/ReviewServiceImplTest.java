package com.isep.acme.services.acme.services;

import com.isep.acme.exceptions.ResourceNotFoundException;
import com.isep.acme.model.Product;
import com.isep.acme.model.Rating;
import com.isep.acme.model.Review;
import com.isep.acme.model.User;
import com.isep.acme.model.dto.CreateReviewDTO;
import com.isep.acme.model.dto.ReviewDTO;
import com.isep.acme.model.dto.VoteReviewDTO;
import com.isep.acme.repositories.ProductRepository;
import com.isep.acme.repositories.ReviewRepository;
import com.isep.acme.repositories.UserRepository;
import com.isep.acme.services.RatingServiceImpl;
import com.isep.acme.services.RestService;
import com.isep.acme.services.ReviewServiceImpl;
import com.isep.acme.services.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ReviewServiceImplTest {
    @Mock
    private ReviewRepository repository;

    @Mock
    private ProductRepository pRepository;

    @Mock
    private UserRepository uRepository;

    @Mock
    private UserServiceImpl userServiceImpl;

    @Mock
    private RatingServiceImpl ratingService;

    @Mock
    private RestService restService;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAll() {
        // Mock behavior of ReviewRepository
        List<Review> reviews = new ArrayList<>();
        when(repository.findAll()).thenReturn(reviews);

        Iterable<Review> result = reviewService.getAll();

        verify(repository).findAll();
        assertSame(reviews, result);
    }

    @Test
    void testCreate() {
        String sku = "123";
        CreateReviewDTO createReviewDTO = new CreateReviewDTO("ReviewText",1L, 4.5);
        LocalDate date = LocalDate.of(2023, 1, 1);
        Rating rating = new Rating(4.5);
        Product product = new Product(sku, "Product Name", "Product Description");
        User user = new User("username", "password");
        user.setUserId(1L);

        when(pRepository.findBySku(sku)).thenReturn(Optional.of(product));
        when(userServiceImpl.getUserId(createReviewDTO.getUserID())).thenReturn(Optional.of(user));
        when(ratingService.findByRate(4.5)).thenReturn(Optional.of(rating));
        when(restService.getFunFact(any())).thenReturn("Fun Fact");

        Review review = new Review("Review text", Date.valueOf(date), product, "Fun Fact", rating, user);
        when(repository.save(review)).thenReturn(review);

        ReviewDTO result = reviewService.create(createReviewDTO, sku);

        //verify(pRepository).findBySku(sku);
        //verify(repository).save(any());
    }

    @Test
    void testGetReviewsOfProduct() {
        String sku = "123";
        Product product = new Product(sku, "Product Name", "Product Description");
        when(pRepository.findBySku(sku)).thenReturn(Optional.of(product));

        List<Review> reviews = new ArrayList<>();
        when(repository.findByProductIdStatus(product, "Approved")).thenReturn(Optional.of(reviews));

        List<ReviewDTO> result = reviewService.getReviewsOfProduct(sku, "Approved");

        verify(pRepository).findBySku(sku);
        verify(repository).findByProductIdStatus(product, "Approved");
        assertEquals(reviews, result);
    }

    @Test
    void testAddVoteToReview() {
        // Mock behavior of ReviewRepository
        Long reviewID = 1L;
        Review review = new Review("Review text", Date.valueOf(LocalDate.now()), new Product("123", "Product Name", "Product Description"), "Fun Fact", new Rating(4.5), new User("username","password"));
        VoteReviewDTO voteReviewDTO = new VoteReviewDTO(1L, "upvote");

        when(repository.findById(reviewID)).thenReturn(Optional.of(review));
        when(repository.save(review)).thenReturn(review);

        boolean result = reviewService.addVoteToReview(reviewID, voteReviewDTO);

        verify(repository).findById(reviewID);
        assertFalse(result);
    }

    @Test
    void testGetWeightedAverage() {
        Product product = new Product("123", "Product Name", "Product Description");
        List<Review> reviews = new ArrayList<>();
        reviews.add(new Review("Review 1", Date.valueOf(LocalDate.now()), product, "Fun Fact 1", new Rating(4.5), new User("username","password")));
        reviews.add(new Review("Review 2", Date.valueOf(LocalDate.now()), product, "Fun Fact 2", new Rating(3.0), new User("username","password")));
        when(repository.findByProductId(product)).thenReturn(Optional.of(reviews));

        double result = reviewService.getWeightedAverage(product);

        verify(repository).findByProductId(product);
        assertEquals(3.75, result);
    }

    @Test
    void testDeleteReview() {
        Long reviewId = 1L;
        Review review = new Review("Review text", Date.valueOf(LocalDate.now()), new Product("123", "Product Name", "Product Description"), "Fun Fact", new Rating(4.5), new User("username","password"));
        when(repository.findById(reviewId)).thenReturn(Optional.of(review));

        boolean result = reviewService.DeleteReview(reviewId);

        verify(repository).findById(reviewId);
        verify(repository).delete(review);
        assertTrue(result);
    }

    @Test
    void testFindPendingReview() {
        List<Review> reviews = new ArrayList<>();
        when(repository.findPendingReviews()).thenReturn(Optional.of(reviews));

        List<ReviewDTO> result = reviewService.findPendingReview();

        verify(repository).findPendingReviews();
        assertEquals(reviews, result);
    }

    @Test
    void testModerateReview() {
        Long reviewID = 1L;
        Review review = new Review("Review text", Date.valueOf(LocalDate.now()), new Product("123", "Product Name", "Product Description"), "Fun Fact", new Rating(4.5), new User("username","password"));

        assertThrows(ResourceNotFoundException.class, () -> reviewService.moderateReview(2L, "Approved"));
        when(repository.findById(reviewID)).thenReturn(Optional.of(review));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> reviewService.moderateReview(reviewID, "InvalidStatus"));
        assertEquals("Invalid status value", exception.getMessage());
        when(repository.save(review)).thenReturn(review);

        ReviewDTO result = reviewService.moderateReview(reviewID, "Approved");

        verify(repository, times(2)).findById(reviewID);
        verify(repository).save(review);
        assertNotNull(result);
    }

    @Test
    void testFindReviewsByUser() {
        Long userID = 1L;
        User user = new User("username","password");
        when(uRepository.findById(userID)).thenReturn(Optional.of(user));

        List<Review> reviews = new ArrayList<>();
        when(repository.findByUserId(user)).thenReturn(Optional.of(reviews));

        List<ReviewDTO> result = reviewService.findReviewsByUser(userID);

        verify(uRepository).findById(userID);
        verify(repository).findByUserId(user);
        assertEquals(reviews, result);
    }

    @Test
    void testGetRecomendedReviews() {
    }
}
