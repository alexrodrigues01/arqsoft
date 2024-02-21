package com.isep.acme.repositories.neo4J;

import com.isep.acme.model.*;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ReviewRepositoryNeo4J extends Neo4jRepository<ReviewNeo4J, Long> {
        @Query("MATCH (r:Review)-[:REVIEWED_PRODUCT]->(p:Product) WHERE ID(p) = $product RETURN r ORDER BY r.publishingDate DESC")
        Optional<List<ReviewNeo4J>> findByProductId(@Param("product") Long product);

        @Query("MATCH (r:Review) WHERE r.approvalStatus = 'pending' RETURN r")
        Optional<List<ReviewNeo4J>> findPendingReviews();

        @Query("MATCH (r:Review) WHERE r.approvalStatus = 'active' RETURN r")
        List<ReviewNeo4J> findActiveReviews();

        @Query("MATCH (r:Review)-[:REVIEWED_PRODUCT]->(p:Product) WHERE r.approvalStatus = $status and ID(p) = $product RETURN r ORDER BY r.publishingDate DESC")
        Optional<List<ReviewNeo4J>> findByProductIdStatus(@Param("product") Long product, @Param("status") String status);
        @Query("MATCH (r:Review)-[:REVIEWED_BY]->(u:User) WHERE ID(u)=$user RETURN r ORDER BY r.publishingDate DESC")
        Optional<List<ReviewNeo4J>> findByUserId(@Param("user") Long user);

        @Query("MATCH (r:Review)-[:REVIEWED_BY]->(u:User {userId: $userID}) RETURN r ORDER BY r.publishingDate DESC")
        List<ReviewNeo4J> findByUserIdRecommendation(@Param("userID") Long userID);

}
