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
    public Optional<List<Vote>> getVoteByReview(Long reviewId) {
        String cypherQuery = "MATCH (v:Vote) WHERE v.reviewId = reviewId RETURN v";
        Map<String, Object> parameters = Map.of("reviewId", reviewId);
        return Optional.of(neo4jRepository.findAll(cypherQuery, parameters, Vote.class));
    }
}
