package com.isep.acme.services.redis;

import com.isep.acme.model.Rating;
import com.isep.acme.model.RatingRedis;
import com.isep.acme.repositories.redis.RatingRepositoryRedis;
import com.isep.acme.services.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RatingServiceRedisImpl implements RatingService {

    @Autowired
    RatingRepositoryRedis repository;

    public Optional<Rating> findByRate(Double rate){
        Optional<RatingRedis> ratingRedis= repository.findByRate(rate);
        Rating rating= null;
        if(ratingRedis.isPresent()){
            rating= new Rating(ratingRedis.get().getRate());
        }
        return Optional.ofNullable(rating);
    }

}