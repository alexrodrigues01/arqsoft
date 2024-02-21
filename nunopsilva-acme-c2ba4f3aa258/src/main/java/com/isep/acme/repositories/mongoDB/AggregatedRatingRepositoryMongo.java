package com.isep.acme.repositories.mongoDB;

import com.isep.acme.controllers.FileController;
import com.isep.acme.model.AggregatedRating;
import com.isep.acme.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AggregatedRatingRepositoryMongo extends MongoRepository<AggregatedRating,Long> {
    Logger logger = LoggerFactory.getLogger(FileController.class);

    @Query("SELECT a FROM AggregatedRating a WHERE a.product=:product")
    Optional<AggregatedRating> findByProduct(Product product);

    Optional<AggregatedRating> findByAggregatedId(Long aggregatedId);

    @Override
    default  <S extends AggregatedRating> S save(S entity){
        final Optional<AggregatedRating> optionalAggregatedRating = findByAggregatedId(entity.getAggregatedId());
        if (optionalAggregatedRating.isEmpty()){
            return insert(entity);
        }

        logger.warn("Product already exists");
        return null;
    }
}
