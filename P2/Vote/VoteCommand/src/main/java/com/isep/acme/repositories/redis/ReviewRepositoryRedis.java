package com.isep.acme.repositories.redis;

import com.isep.acme.model.Review;
import com.isep.acme.repositories.ReviewRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository("ReviewRepositoryRedis")
public class ReviewRepositoryRedis extends RedisBaseRepository<Review, Long> implements ReviewRepository {

    public ReviewRepositoryRedis(final RedisTemplate<String, Object> redisTemplate) {
        super(redisTemplate, Review.class);
    }

    @Override
    public Optional<Review> findById(Long reviewId) {
        return hashOperations.entries(Review.class.getSimpleName()).values().stream()
                .filter(review -> review.getId().equals(reviewId))
                .findAny();
    }
}
