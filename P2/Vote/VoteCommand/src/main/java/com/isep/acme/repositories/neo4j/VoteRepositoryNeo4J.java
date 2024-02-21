package com.isep.acme.repositories.neo4j;

import com.isep.acme.model.User;
import com.isep.acme.model.Vote;
import com.isep.acme.repositories.VoteRepository;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository("VoteRepositoryNeo4J")
public class VoteRepositoryNeo4J extends Neo4JBaseRepository<Vote, Long> implements VoteRepository {

    public VoteRepositoryNeo4J(final Neo4jTemplate neo4jTemplate) {
        super(neo4jTemplate, Vote.class);
    }

    @Override
    public boolean findVote(Vote vote){
        String cypherQuery = "MATCH (v:Vote) WHERE v.reviewId = $reviewId AND v.userId = $userId AND v.vote = $vote RETURN v";
        Map<String, Object> parameters = Map.of("reviewId", vote.getReviewId(), "userId",vote.getUserId(),"vote",vote.getVote());
        Class<Vote> domainType = Vote.class;

        if(neo4jRepository.findOne(cypherQuery, parameters, domainType).isEmpty()){
            return false;
        }else{
            return true;
        }
    }
}
