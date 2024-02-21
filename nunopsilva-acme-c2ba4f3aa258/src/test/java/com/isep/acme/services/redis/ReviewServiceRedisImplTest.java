package com.isep.acme.services.redis;

import com.isep.acme.controllers.ResourceNotFoundException;
import com.isep.acme.model.*;
import com.isep.acme.model.dto.CreateReviewDTO;
import com.isep.acme.model.dto.ReviewDTO;
import com.isep.acme.model.dto.VoteReviewDTO;
import com.isep.acme.repositories.redis.ProductRepositoryRedis;
import com.isep.acme.repositories.redis.ReviewRepositoryRedis;
import com.isep.acme.repositories.redis.UserRepositoryRedis;
import com.isep.acme.services.RestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class ReviewServiceRedisImplTest {

    @Mock
    private ReviewRepositoryRedis repository;

    @Mock
    private ProductRepositoryRedis pRepository;

    @Mock
    private UserRepositoryRedis uRepository;

    @Mock
    private UserServiceRedisImpl userServiceImpl;

    @Mock
    private RatingServiceRedisImpl ratingService;

    @Mock
    private RestService restService;

    @Mock
    private StringRedisTemplate stringRedisTemplate;

    @InjectMocks
    private ReviewServiceRedisImpl reviewServiceRedis;

    private ReviewRedis review;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        ProductRedis product = new ProductRedis(1L, "123456123456", "Product 1", "Description 1");
        UserRedis user = new UserRedis("username", "password", "Full Name", "123451234", "Address");
        RatingRedis rating = new RatingRedis(2.0);
        review = new ReviewRedis(1L ,"Review Text", LocalDate.now(), product, "Fun Fact", rating, user);
    }

//    @Test
//    void testGetAll() {
//        when(repository.findAll()).thenReturn(Collections.singletonList(review));
//        Iterable<Review> reviews = reviewServiceRedis.getAll();
//        assertTrue(reviews.iterator().hasNext());
//        Review retrievedReview = reviews.iterator().next();
//        assertEquals(review.getReviewText(), retrievedReview.getReviewText());
//        when(stringRedisTemplate.opsForList().range(anyString(), any(), any())).thenReturn(List.of());
//        assertEquals(review.getPublishingDate(), retrievedReview.getPublishingDate());
//    }
//
//    @Test
//    void testCreateReview() {
//        CreateReviewDTO createReviewDTO = new CreateReviewDTO();
//        createReviewDTO.setUserID(1L);
//
//        ProductRedis product = new ProductRedis(1L, "SKU123", "Product 1", "Description 1");
//        when(pRepository.findBySku(any(String.class))).thenReturn(Optional.of(product));
//
//        UserRedis user = new UserRedis("username", "password", "Full Name", "12345", "Address");
//        when(userServiceImpl.getUserId(1L)).thenReturn(Optional.of(user));
//
//        when(ratingService.getRatingByRate(0.0)).thenReturn(Optional.of(new RatingRedis(0.0)));
//
//        LocalDate date = LocalDate.now();
//        when(restService.getFunFact(date)).thenReturn("A fun fact");
//
//        ReviewDTO reviewDTO = reviewServiceRedis.create(createReviewDTO, "SKU123");
//        assertNotNull(reviewDTO);
//        assertEquals("Review Text", reviewDTO.getReviewText());
//        assertEquals(date, reviewDTO.getPublishingDate());
//        // Verify other assertions for the created review
//    }
//
//    @Test
//    void testGetReviewsOfProduct() {
//        when(pRepository.findBySku(any(String.class))).thenReturn(Optional.of(review.getProduct()));
//        when(repository.findByProductSkuAndApprovalStatus(any(String.class), any(String.class)))
//                .thenReturn(Collections.singletonList(review));
//        List<ReviewDTO> reviews = reviewServiceRedis.getReviewsOfProduct("SKU123", "approved");
//        assertFalse(reviews.isEmpty());
//        ReviewDTO retrievedReview = reviews.get(0);
//        assertEquals(review.getReviewText(), retrievedReview.getReviewText());
//        assertEquals(review.getPublishingDate(), retrievedReview.getPublishingDate());
//        // Verify other assertions for review attributes
//    }
//
//    @Test
//    void testAddVoteToReview() {
//        when(repository.findById(any(String.class))).thenReturn(Optional.of(review));
//
//        VoteReviewDTO upVoteDTO = new VoteReviewDTO("upVote", 1L);
//        boolean added = reviewServiceRedis.addVoteToReview(1L, upVoteDTO);
//        assertTrue(added);
//        assertEquals(1, review.getUpVotes().size());
//
//        VoteReviewDTO downVoteDTO = new VoteReviewDTO("downVote", 2L);
//        added = reviewServiceRedis.addVoteToReview(1L, downVoteDTO);
//        assertTrue(added);
//        assertEquals(1, review.getDownVotes().size());
//    }
//
//    @Test
//    void testGetWeightedAverage() {
//        ProductRedis product = new ProductRedis(1L, "SKU123", "Product 1", "Description 1");
//        when(repository.findByProductId(any(ProductRedis.class)).thenReturn(Collections.singletonList(review));
//        double average = reviewServiceRedis.getWeightedAverage(product);
//        assertEquals(2.0, average);
//    }
//
//    @Test
//    void testDeleteReview() {
//        when(repository.findById(any(String.class))).thenReturn(Optional.of(review));
//
//        boolean deleted = reviewServiceRedis.DeleteReview(1L);
//        assertTrue(deleted);
//
//        when(repository.findById(any(String.class))).thenReturn(Optional.empty());
//        deleted = reviewServiceRedis.DeleteReview(1L);
//        assertFalse(deleted);
//    }
//
//    @Test
//    void testFindPendingReview() {
//        when(repository.findByApprovalStatus("pending")).thenReturn(Collections.singletonList(review));
//        List<ReviewDTO> reviews = reviewServiceRedis.findPendingReview();
//        assertFalse(reviews.isEmpty());
//        ReviewDTO retrievedReview = reviews.get(0);
//        assertEquals(review.getReviewText(), retrievedReview.getReviewText());
//        assertEquals(review.getPublishingDate(), retrievedReview.getPublishingDate());
//        // Verify other assertions for review attributes
//    }
//
//    @Test
//    void testModerateReview() {
//        when(repository.findById(any(String.class))).thenReturn(Optional.of(review));
//
//        try {
//            ReviewDTO moderatedReview = reviewServiceRedis.moderateReview(1L, "approved");
//            assertNotNull(moderatedReview);
//            assertEquals("approved", moderatedReview.getApprovalStatus());
//
//            moderatedReview = reviewServiceRedis.moderateReview(1L, "rejected");
//            assertNotNull(moderatedReview);
//            assertEquals("rejected", moderatedReview.getApprovalStatus());
//
//            assertThrows(IllegalArgumentException.class, () -> reviewServiceRedis.moderateReview(1L, "invalidStatus"));
//        } catch (ResourceNotFoundException e) {
//            fail("ResourceNotFoundException not expected");
//        }
//    }
//
//    @Test
//    void testFindReviewsByUser() {
//        when(uRepository.findById(1L)).thenReturn(Optional.of(review.getUser()));
//        when(repository.findByUserUserId(1L)).thenReturn(Collections.singletonList(review));
//        List<ReviewDTO> reviews = reviewServiceRedis.findReviewsByUser(1L);
//        assertFalse(reviews.isEmpty());
//        ReviewDTO retrievedReview = reviews.get(0);
//        assertEquals(review.getReviewText(), retrievedReview.getReviewText());
//        assertEquals(review.getPublishingDate(), retrievedReview.getPublishingDate());
//        // Verify other assertions for review attributes
//    }
}
