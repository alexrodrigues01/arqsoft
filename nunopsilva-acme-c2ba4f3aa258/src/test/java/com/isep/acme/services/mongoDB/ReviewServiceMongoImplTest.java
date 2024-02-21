package com.isep.acme.services.mongoDB;

import com.isep.acme.controllers.ResourceNotFoundException;
import com.isep.acme.model.*;
import com.isep.acme.model.dto.CreateReviewDTO;
import com.isep.acme.model.dto.ReviewDTO;
import com.isep.acme.model.dto.VoteReviewDTO;
import com.isep.acme.repositories.mongoDB.ProductRepositoryMongo;
import com.isep.acme.repositories.mongoDB.ReviewRepositoryMongo;
import com.isep.acme.repositories.mongoDB.UserRepositoryMongo;
import com.isep.acme.services.RestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewServiceMongoImplTest {

    @Mock
    private ReviewRepositoryMongo repository;

    @Mock
    private ProductRepositoryMongo pRepository;

    @Mock
    private UserRepositoryMongo uRepository;

    @Mock
    private UserServiceMongoImpl userServiceImpl;

    @Mock
    private RatingServiceMongoImpl ratingService;

    @Mock
    private RestService restService;

    @InjectMocks
    private ReviewServiceMongoImpl reviewService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAll() {
        List<ReviewMongo> reviews = new ArrayList<>();
        when(repository.findAll()).thenReturn(reviews);

        Iterable<Review> result = reviewService.getAll();

        verify(repository).findAll();
        assertFalse(result.iterator().hasNext());
    }

    @Test
    void testCreate() {
        String sku = "123";
        CreateReviewDTO createReviewDTO = new CreateReviewDTO("ReviewText", 1L, 4.5);
        LocalDate date = LocalDate.of(2023, 1, 1);
        RatingMongo rating = new RatingMongo(4.5);
        ProductMongo product = new ProductMongo(1L,sku, "Product Name", "Product Description");
        UserMongo user = new UserMongo("username", "password", "FullName", "123456789", "morada");
        user.setUserId(1L);

        when(pRepository.findBySku(sku)).thenReturn(Optional.of(product));
        when(userServiceImpl.getUserId(1L)).thenReturn(Optional.of(new User("username", "password", "FullName", "123456789", "morada")));
        when(ratingService.findByRate(4.5)).thenReturn(Optional.of( new Rating(4.5)));
        when(restService.getFunFact(any())).thenReturn("Fun Fact");

        ReviewMongo review = new ReviewMongo("Review text", date, product, "Fun Fact", rating, user);
        when(repository.save(review)).thenReturn(review);

        ReviewDTO result = reviewService.create(createReviewDTO, sku);

        verify(pRepository).findBySku(sku);
        verify(userServiceImpl).getUserId(1L);
        verify(ratingService).findByRate(4.5);
        verify(repository).save(any());
    }

    @Test
    void testCreateProductNotFound() {
        String sku = "123";
        CreateReviewDTO createReviewDTO = new CreateReviewDTO("ReviewText", 1L, 4.5);

        when(pRepository.findBySku(sku)).thenReturn(Optional.empty());

        ReviewDTO result = reviewService.create(createReviewDTO, sku);

        verify(pRepository).findBySku(sku);
        assertNull(result);
    }

    @Test
    void testCreateUserNotFound() {
        String sku = "123";
        CreateReviewDTO createReviewDTO = new CreateReviewDTO("ReviewText", 1L, 4.5);
        ProductMongo product = new ProductMongo(sku, "Product Name", "Product Description");

        when(pRepository.findBySku(sku)).thenReturn(Optional.of(product));
        when(userServiceImpl.getUserId(1L)).thenReturn(Optional.empty());

        ReviewDTO result = reviewService.create(createReviewDTO, sku);

        verify(pRepository).findBySku(sku);
        verify(userServiceImpl).getUserId(1L);
        assertNull(result);
    }

    @Test
    void testCreateRatingNotFound() {
        String sku = "123";
        CreateReviewDTO createReviewDTO = new CreateReviewDTO("ReviewText", 1L, 4.5);
        ProductMongo product = new ProductMongo(sku, "Product Name", "Product Description");
        User user = new User("username", "password");
        user.setUserId(1L);

        when(pRepository.findBySku(sku)).thenReturn(Optional.of(product));
        when(userServiceImpl.getUserId(1L)).thenReturn(Optional.of(user));
        when(ratingService.findByRate(4.5)).thenReturn(Optional.empty());

        ReviewDTO result = reviewService.create(createReviewDTO, sku);

        verify(pRepository).findBySku(sku);
        verify(userServiceImpl).getUserId(1L);
        verify(ratingService).findByRate(4.5);
        assertNull(result);
    }

    @Test
    void testCreateFunFactNotFound() {
        String sku = "123";
        CreateReviewDTO createReviewDTO = new CreateReviewDTO("ReviewText", 1L, 4.5);
        LocalDate date = LocalDate.of(2023, 1, 1);
        Rating rating = new Rating(4.5);
        ProductMongo product = new ProductMongo(sku, "Product Name", "Product Description");
        User user = new User("username", "password");
        user.setUserId(1L);

        when(pRepository.findBySku(sku)).thenReturn(Optional.of(product));
        when(userServiceImpl.getUserId(1L)).thenReturn(Optional.of(user));
        when(ratingService.findByRate(4.5)).thenReturn(Optional.of(rating));
        when(restService.getFunFact(any())).thenReturn(null);

        ReviewDTO result = reviewService.create(createReviewDTO, sku);

        verify(pRepository).findBySku(sku);
        verify(userServiceImpl).getUserId(1L);
        verify(ratingService).findByRate(4.5);
        verify(restService).getFunFact(any());
        assertNull(result);
    }

    @Test
    void testGetReviewsOfProduct() {
        String sku = "123";
        ProductMongo product = new ProductMongo(sku, "Product Name", "Product Description");
        when(pRepository.findBySku(sku)).thenReturn(Optional.of(product));

        List<ReviewMongo> reviews = new ArrayList<>();
        when(repository.findByProductAndApprovalStatus(product, "Approved")).thenReturn(Optional.of(reviews));

        List<ReviewDTO> result = reviewService.getReviewsOfProduct(sku, "Approved");

        verify(pRepository).findBySku(sku);
        verify(repository).findByProductAndApprovalStatus(product, "Approved");
        assertEquals(new ArrayList<>(), result);
    }

    @Test
    void testAddVoteToReview() {
        Long reviewID = 1L;
        ReviewMongo review = new ReviewMongo("Review text", LocalDate.now(), new ProductMongo("123", "Product Name", "Product Description"), "Fun Fact", new RatingMongo(4.5), new UserMongo("username", "password"));
        VoteReviewDTO voteReviewDTO = new VoteReviewDTO(1L, "upVote");

        when(repository.findById(reviewID)).thenReturn(Optional.of(review));
        when(repository.save(review)).thenReturn(review);

        boolean result = reviewService.addVoteToReview(reviewID, voteReviewDTO);

        verify(repository).findById(reviewID);
        assertFalse(result);
    }

    @Test
    void testAddVoteToReviewDownVote() {
        Long reviewID = 1L;
        ReviewMongo review = new ReviewMongo("Review text", LocalDate.now(), new ProductMongo("123", "Product Name", "Product Description"), "Fun Fact", new RatingMongo(4.5), new UserMongo("username", "password"));
        VoteReviewDTO voteReviewDTO = new VoteReviewDTO(1L, "downVote");

        when(repository.findById(reviewID)).thenReturn(Optional.of(review));
        when(repository.save(review)).thenReturn(review);

        boolean result = reviewService.addVoteToReview(reviewID, voteReviewDTO);

        verify(repository).findById(reviewID);
        assertFalse(result);
    }

    @Test
    void testGetWeightedAverage() {
        ProductMongo product = new ProductMongo(1L,"123", "Product Name", "Product Description");
        List<ReviewMongo> reviews = new ArrayList<>();
        reviews.add(new ReviewMongo(1L,"Review 1", LocalDate.now(), product, "Fun Fact 1", new RatingMongo(4.5), new UserMongo("username", "password")));
        reviews.add(new ReviewMongo(2L,"Review 2", LocalDate.now(), product, "Fun Fact 2", new RatingMongo(3.0), new UserMongo("username", "password")));
        when(repository.findByProduct(eq(product))).thenReturn(Optional.of(reviews));

        double result = reviewService.getWeightedAverage(new Product(1L, "123", "Product Name", "Product Description"));

        verify(repository).findByProduct(eq(product));
        assertEquals(3.75, result);
    }

    @Test
    void testDeleteReview() {
        Long reviewId = 1L;
        ReviewMongo review = new ReviewMongo("Review text", LocalDate.now(), new ProductMongo("123", "Product Name", "Product Description"), "Fun Fact", new RatingMongo(4.5), new UserMongo("username", "password"));
        when(repository.findById(reviewId)).thenReturn(Optional.of(review));

        boolean result = reviewService.DeleteReview(reviewId);

        verify(repository).findById(reviewId);
        verify(repository).delete(review);
        assertTrue(result);
    }

    @Test
    void testDeleteReviewWithVotes() {
        Long reviewId = 1L;
        ReviewMongo review = new ReviewMongo("Review text", LocalDate.now(), new ProductMongo("123", "Product Name", "Product Description"), "Fun Fact", new RatingMongo(4.5), new UserMongo("username", "password"));
        review.setApprovalStatus("approved");
        Vote vote = new Vote("upVote", 1L);
        review.addUpVote(vote);

        when(repository.findById(reviewId)).thenReturn(Optional.of(review));

        boolean result = reviewService.DeleteReview(reviewId);

        verify(repository).findById(reviewId);
        verify(repository, never()).delete(review);
        assertFalse(result);
    }

    @Test
    void testFindPendingReview() {
        List<ReviewMongo> reviews = new ArrayList<>();
        when(repository.getReviewByApprovalStatus("pending")).thenReturn(Optional.of(reviews));

        List<ReviewDTO> result = reviewService.findPendingReview();

        verify(repository).getReviewByApprovalStatus("pending");
        assertEquals(new ArrayList<>(), result);
    }

    @Test
    void testModerateReview() {
        Long reviewID = 1L;
        ReviewMongo review = new ReviewMongo("Review text", LocalDate.now(), new ProductMongo("123", "Product Name", "Product Description"), "Fun Fact", new RatingMongo(4.5), new UserMongo("username", "password"));
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

}