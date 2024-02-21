package com.isep.acme.repositories.neo4J;

import com.isep.acme.model.RatingNeo4J;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RatingRepositoryNeo4J extends Neo4jRepository<RatingNeo4J, Long> {

    @Query("MATCH (r:RatingNeo4J) WHERE r.rate = $rate RETURN r")
    Optional<RatingNeo4J> findByRate(@Param("rate") Double rate);

}
