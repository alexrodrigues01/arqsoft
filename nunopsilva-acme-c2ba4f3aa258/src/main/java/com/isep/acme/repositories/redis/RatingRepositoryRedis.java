package com.isep.acme.repositories.redis;

import com.isep.acme.model.RatingRedis;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RatingRepositoryRedis extends CrudRepository<RatingRedis, Long> {

    @Query("SELECT r FROM Rating r WHERE r.rate=:rate")
    Optional<RatingRedis> findByRate(Double rate);

}