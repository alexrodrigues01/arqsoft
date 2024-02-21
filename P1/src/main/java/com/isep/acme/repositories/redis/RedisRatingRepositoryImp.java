package com.isep.acme.repositories.redis;

import com.isep.acme.model.Rating;
import com.isep.acme.repositories.RatingRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("RedisRatingRepositoryImp")
public class RedisRatingRepositoryImp extends RedisBaseRepository<Rating, Long> implements RatingRepository {

    public RedisRatingRepositoryImp(final RedisTemplate<String, Object> redisTemplate) {
        super(redisTemplate, Rating.class);
    }

    @Override
    public Optional<Rating> findByRate(Double rate) {
        return hashOperations.entries(Rating.class.getSimpleName()).values().stream()
                .filter(rating -> rating.getRate().equals(rate))
                .findAny();
    }
}
