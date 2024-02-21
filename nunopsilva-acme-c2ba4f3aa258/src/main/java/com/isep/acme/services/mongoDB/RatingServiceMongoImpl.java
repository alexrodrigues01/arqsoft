package com.isep.acme.services.mongoDB;

import com.isep.acme.model.Rating;
import com.isep.acme.repositories.mongoDB.RatingRepositoryMongo;
import com.isep.acme.services.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RatingServiceMongoImpl implements RatingService {
    @Autowired
    RatingRepositoryMongo repository;

    public Optional<Rating> findByRate(Double rate) {
        return repository.findByRate(rate);
    }
}