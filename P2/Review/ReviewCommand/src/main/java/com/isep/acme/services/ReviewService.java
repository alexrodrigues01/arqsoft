package com.isep.acme.services;

import com.isep.acme.model.Product;
import com.isep.acme.model.review.CreateReviewDTO;
import com.isep.acme.model.review.ReviewDTO;

public interface ReviewService {


    ReviewDTO create(CreateReviewDTO createReviewDTO, String sku);

    Double getWeightedAverage(Product product);

    Boolean deleteReview(Long reviewId);

    ReviewDTO moderateReview(Long reviewID, String approved, Long userId);

}
