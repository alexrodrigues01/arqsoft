package com.isep.acme.repositories.mongoDB;

import com.isep.acme.model.Vote;
import com.isep.acme.repositories.VoteRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("VoteRepositoryMongo")
public class VoteRepositoryMongo extends MongoBaseRepository<Vote, Long> implements VoteRepository {
    public VoteRepositoryMongo(final MongoTemplate mongoTemplate) {
        super(mongoTemplate, Vote.class);
    }

    @Override
    public Optional<List<Vote>> getVoteByReview(Long reviewId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("reviewId").is(reviewId));
        return Optional.of(mongoTemplate.find(query, Vote.class));
    }

}
