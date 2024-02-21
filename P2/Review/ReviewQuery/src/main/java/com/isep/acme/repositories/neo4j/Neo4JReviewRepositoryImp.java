package com.isep.acme.repositories.neo4j;

import com.isep.acme.model.Product;
import com.isep.acme.model.Review;
import com.isep.acme.model.user.User;
import com.isep.acme.repositories.ReviewRepository;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository("Neo4JReviewRepositoryImp")
public class Neo4JReviewRepositoryImp extends Neo4JBaseRepository<Review, Long> implements ReviewRepository {

    public Neo4JReviewRepositoryImp(final Neo4jTemplate neo4jTemplate) {
        super(neo4jTemplate, Review.class);
    }


    @Override
    public Optional<List<Review>> findByProductId(Product product) {
        String cypherQuery = "MATCH (product:Product)<-[:HAS_PRODUCT]-(review:Review) WHERE product.productID = $id RETURN review";
        Map<String, Object> parameters = Map.of("id", product.getProductID());
        List<Review> list = neo4jRepository.findAll(cypherQuery, parameters, Review.class);
        return Optional.of(list);
    }

    @Override
    public Optional<List<Review>> findPendingReviews() {
        String cypherQuery = "MATCH (r:Review) WHERE r.approvalStatus='pending' RETURN r";
        return Optional.of(neo4jRepository.findAll(cypherQuery, Review.class));
    }

    @Override
    public Optional<List<Review>> findActiveReviews() {
        String cypherQuery = "MATCH (r:Review) WHERE r.approvalStatus='approved' RETURN r";
        List<Review> list = neo4jRepository.findAll(cypherQuery, Review.class);

        for (Review r : list) {
            String cypherQueryUser = "MATCH (u:User)<-[:HAS_USER]-(r:Review) WHERE r.idReview = $idReview RETURN u";
            Map<String, Object> parametersUser = Map.of("idReview", r.getIdReview());
            User u = neo4jRepository.findOne(cypherQueryUser, parametersUser, User.class).orElse(null);
            r.setUser(u);
        }

        return Optional.of(list);

    }


    @Override
    public Optional<List<Review>> findByProductIdStatus(Product product, String status) {
        String cypherQuery = "MATCH (product:Product)<-[:HAS_PRODUCT]-(review:Review) WHERE product.productID = $id AND review.approvalStatus = $status RETURN review ORDER BY review.publishingDate";
        Map<String, Object> parameters = Map.of("id", product.getProductID(), "status", status);
        List<Review> list = neo4jRepository.findAll(cypherQuery, parameters, Review.class);
        return Optional.of(list);
    }

    @Override
    public Optional<List<Review>> findByUserId(User user) {
        String cypherQuery = "MATCH (u:User)<-[:HAS_USER]-(r:Review) WHERE u.userId = $id RETURN r";
        Map<String, Object> parameters = Map.of("id", user.getUserId());
        List<Review> list = neo4jRepository.findAll(cypherQuery, parameters, Review.class);
        return Optional.of(list);
    }

    @Override
    public Optional<List<Review>> findReviewsVotedByUserId(Long userId) {
     /*   String cypherQueryUpVote = "MATCH (v:Vote)<-[:HAS_UPVOTE]-(r:Review) WHERE v.userID = $userId RETURN r";
        String cypherQueryDownVote = "MATCH (v:Vote)<-[:HAS_DOWNVOTE]-(r:Review) WHERE v.userID = $userId RETURN r";
        Map<String, Object> parameters = Map.of("userId", userId);
        List<Review> upVoteList = neo4jRepository.findAll(cypherQueryUpVote, parameters, Review.class);
        List<Review> downVoteList = neo4jRepository.findAll(cypherQueryDownVote, parameters, Review.class);
        Set<Review> set = new HashSet<>();
        set.addAll(upVoteList);
        set.addAll(downVoteList);
        return Optional.of(new ArrayList<>(set));*/


        String cypherQuery = "MATCH (r:Review)-[:HAS_UPVOTE]->(v:Vote) WHERE v.userID = $userId AND r.approvalStatus = 'approved' RETURN r " +
                "UNION " +
                "MATCH (r:Review)-[:HAS_DOWNVOTE]->(v:Vote) WHERE v.userID = $userId AND r.approvalStatus = 'approved' RETURN r";

        Map<String, Object> parameters = Map.of("userId", userId);
        List<Review> votedReviews = neo4jRepository.findAll(cypherQuery, parameters, Review.class);
        return Optional.of(votedReviews);
    }
}