package com.isep.acme.repositories.mongo;

import com.isep.acme.model.user.User;
import com.isep.acme.repositories.UserRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository("MongoUserRepositoryImp")
public class MongoUserRepositoryImp extends MongoBaseRepository<User, Long> implements UserRepository {

    public MongoUserRepositoryImp(final MongoTemplate mongoTemplate) {
        super(mongoTemplate, User.class);
    }

    @Override
    public User getById(Long userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        return mongoTemplate.find(query, User.class).stream().findAny().orElse(null);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username));
        return mongoTemplate.find(query, User.class).stream().findAny();
    }
}
