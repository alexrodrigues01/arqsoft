package com.isep.acme.repositories.mongo;

import com.isep.acme.model.Rating;
import com.isep.acme.repositories.RatingRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("MongoRatingRepositoryImp")
public class MongoRatingRepositoryImp extends MongoBaseRepository<Rating, Long> implements RatingRepository {

    public MongoRatingRepositoryImp(final MongoTemplate mongoTemplate) {
        super(mongoTemplate, Rating.class);
    }

    @Override
    public Optional<Rating> findByRate(Double rate) {
        Query query = new Query();
        query.addCriteria(Criteria.where("rate").is(rate));
        return mongoTemplate.find(query, Rating.class).stream().findFirst();
    }
}
