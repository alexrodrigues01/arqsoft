package com.isep.acme.repositories.neo4j;

import com.isep.acme.model.Role;
import com.isep.acme.model.User;
import com.isep.acme.repositories.UserRepository;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository("UserRepositoryNeo4J")
public class UserRepositoryNeo4J extends Neo4JBaseRepository<User,Long> implements UserRepository {

    public UserRepositoryNeo4J(final Neo4jTemplate neo4jTemplate) {
        super(neo4jTemplate, User.class);
    }

    @Override
    public User getById(Long userId) {
        return neo4jRepository.findById(userId, User.class).orElse(null);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String cypherQuery = "MATCH (u:User) WHERE u.username = $username RETURN u";
        String cypherQueryRoles = "MATCH (u:User)-[:HAS_ROLES]->(r:Role) WHERE u.username = $username RETURN r";

        Map<String, Object> parameters = Map.of("username", username);
        User user =  neo4jRepository.findOne(cypherQuery, parameters, User.class).orElse(null);

        if (user != null) {
            user.setAuthorities(neo4jRepository.findAll(cypherQueryRoles, parameters, Role.class));
            return Optional.of(user);
        }
        return Optional.empty();
    }
}
