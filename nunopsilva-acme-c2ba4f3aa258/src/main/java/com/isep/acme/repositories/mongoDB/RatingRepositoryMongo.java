package com.isep.acme.repositories.mongoDB;

import com.isep.acme.controllers.FileController;
import com.isep.acme.model.Rating;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RatingRepositoryMongo extends MongoRepository<Rating,Long> {
    Logger logger = LoggerFactory.getLogger(FileController.class);
    @Query("SELECT r FROM Rating r WHERE r.rate=:rate")
    Optional<Rating> findByRate(Double rate);



    @Override
    default  <S extends Rating> S save(S entity){
        final Optional<Rating> optionalRating = findByRate(entity.getRate());
        if (optionalRating.isEmpty()){
            return insert(entity);
        }

        logger.warn("Rating already exists");
        return null;
    }


}
