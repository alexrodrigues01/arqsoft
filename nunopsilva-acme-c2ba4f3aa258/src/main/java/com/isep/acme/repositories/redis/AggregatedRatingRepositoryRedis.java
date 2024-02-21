package com.isep.acme.repositories.redis;

import com.isep.acme.model.AggregatedRatingRedis;
import com.isep.acme.model.ProductRedis;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AggregatedRatingRepositoryRedis extends CrudRepository<AggregatedRatingRedis, Long> {

    //@Query("SELECT a FROM AggregatedRating a WHERE a.product=:product")
    Optional<AggregatedRatingRedis> findByProductId(ProductRedis product);
}
