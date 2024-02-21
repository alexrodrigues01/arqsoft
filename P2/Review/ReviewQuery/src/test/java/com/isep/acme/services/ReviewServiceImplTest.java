package com.isep.acme.services;

import com.isep.acme.model.Product;
import com.isep.acme.model.Review;
import com.isep.acme.model.review.ReviewDTO;
import com.isep.acme.model.user.User;
import com.isep.acme.repositories.ProductRepository;
import com.isep.acme.repositories.ReviewRepository;
import com.isep.acme.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class ReviewServiceImplTest {
    @Mock
    private ReviewRepository repository;

    @Mock
    private ProductRepository pRepository;

    @Mock
    private UserRepository uRepository;

    @Mock
    private UserService userServiceImpl;

    @Mock
    private RatingServiceImpl ratingService;

    @Mock
    private RestService restService;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @Autowired
    private ApplicationContext applicationContext;

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
    void testGetReviewsOfProduct() {
        String sku = "123456789";
        Product product = new Product(sku);
        when(pRepository.findBySku(sku)).thenReturn(Optional.of(product));

        List<Review> reviews = new ArrayList<>();
        when(repository.findByProductIdStatus(product, "Approved")).thenReturn(Optional.of(reviews));

        List<ReviewDTO> result = reviewService.getReviewsOfProduct(sku, "Approved");

        verify(pRepository).findBySku(sku);
        verify(repository).findByProductIdStatus(product, "Approved");
        assertEquals(reviews, result);
    }

//
//    @Test
//    void testGetWeightedAverage() {
//        Product product = new Product("123", "Product Name", "Product Description");
//        List<Review> reviews = new ArrayList<>();
//        reviews.add(new Review("Review 1", LocalDate.now(), product, "Fun Fact 1", new Rating(4.5), new User("username","password")));
//        reviews.add(new Review("Review 2", LocalDate.now(), product, "Fun Fact 2", new Rating(3.0), new User("username","password")));
//        when(repository.findByProductId(product)).thenReturn(Optional.of(reviews));
//
//        double result = reviewService.getWeightedAverage(product);
//
//        verify(repository).findByProductId(product);
//        assertEquals(3.75, result);
//    }


    @Test
    void testFindPendingReview() {
        List<Review> reviews = new ArrayList<>();
        when(repository.findPendingReviews()).thenReturn(Optional.of(reviews));

        List<ReviewDTO> result = reviewService.findPendingReview();

        verify(repository).findPendingReviews();
        assertEquals(reviews, result);
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
}
