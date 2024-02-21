package com.isep.acme.services;

import java.util.List;

import com.isep.acme.model.Product;
import com.isep.acme.model.Review;
import com.isep.acme.model.review.AcceptRejectReviewDTO;
import com.isep.acme.model.review.ReviewDTO;
import com.isep.acme.model.review.ReviewQueueDTO;

public interface ReviewService {

    Iterable<Review> getAll();

    List<ReviewDTO> getReviewsOfProduct(String sku, String status);


    Double getWeightedAverage(Product product);

    Boolean delete(Long reviewId);

    List<ReviewDTO> findPendingReview();

    List<ReviewDTO> findReviewsByUser(Long userID);

    List<ReviewDTO> recommendedReviews(Long userId);

    ReviewDTO create(ReviewQueueDTO reviewQueueDTO);;

    ReviewDTO moderateReview(AcceptRejectReviewDTO acceptRejectReviewDTO);

    ReviewDTO updateReview(ReviewQueueDTO reviewQueueDTO);
}
