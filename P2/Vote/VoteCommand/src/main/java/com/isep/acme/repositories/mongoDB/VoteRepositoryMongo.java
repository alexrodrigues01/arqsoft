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
    public boolean findVote(Vote vote){
        Query query = new Query();
        query.addCriteria(Criteria.where("reviewId").is(vote.getReviewId()));
        query.addCriteria(Criteria.where("userId").is(vote.getUserId()));
        query.addCriteria(Criteria.where("vote").is(vote.getVote()));
        if(mongoTemplate.find(query,Vote.class).stream().findFirst().isEmpty()){
            return false;
        }else{
            return true;
        }
    }

}
