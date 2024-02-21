package com.isep.acme.repositories.redis;

import com.isep.acme.model.AggregatedRating;
import com.isep.acme.model.Product;
import com.isep.acme.repositories.AggregatedRatingRepository;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Optional;

public class RedisAggregatedRatingRepositoryImp extends RedisBaseRepository<AggregatedRating, Long> implements AggregatedRatingRepository {

    public RedisAggregatedRatingRepositoryImp(final RedisTemplate<String, Object> redisTemplate) {
        super(redisTemplate, AggregatedRating.class);
    }

    @Override
    public Optional<AggregatedRating> findByProductId(Product product) {
        return hashOperations.entries(AggregatedRating.class.getSimpleName()).values().stream()
                .filter(aggregatedRating -> aggregatedRating.getProduct().getProductID().equals(product.getProductID()))
                .findAny();
    }
}
