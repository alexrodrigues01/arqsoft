package com.isep.acme.repositories.mongoDB;

import com.isep.acme.model.Review;
import com.isep.acme.model.Vote;
import com.isep.acme.repositories.ReviewRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("ReviewRepositoryMongo")
public class ReviewRepositoryMongo extends MongoBaseRepository<Review,Long> implements ReviewRepository {

    public ReviewRepositoryMongo(final MongoTemplate mongoTemplate){super(mongoTemplate,Review.class);}

    @Override
    public Optional<Review> findById(Long reviewId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("idReview").is(reviewId));
        return mongoTemplate.find(query, Review.class).stream().findFirst();
    }
}
