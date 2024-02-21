package com.isep.acme.services;

import java.util.List;

import com.isep.acme.model.dto.CreateReviewDTO;
import com.isep.acme.model.Product;
import com.isep.acme.model.Review;
import com.isep.acme.model.dto.ReviewDTO;
import com.isep.acme.model.dto.VoteReviewDTO;

public interface ReviewService {

    Iterable<Review> getAll();

    List<ReviewDTO> getReviewsOfProduct(String sku, String status);

    ReviewDTO create(CreateReviewDTO createReviewDTO, String sku);

    boolean addVoteToReview(Long reviewID, VoteReviewDTO voteReviewDTO);

    Double getWeightedAverage(Product product);

    Boolean DeleteReview(Long reviewId);

    List<ReviewDTO> findPendingReview();

    ReviewDTO moderateReview(Long reviewID, String approved);

    List<ReviewDTO> findReviewsByUser(Long userID);

    List<ReviewDTO> recommendedReviews(Long userId);
}
