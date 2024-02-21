package com.isep.acme.services;

import com.isep.acme.model.Review;

import java.util.List;

public interface ReviewRecommendation {
    List<Review> getReviewsRecommended(Long userId);
}
