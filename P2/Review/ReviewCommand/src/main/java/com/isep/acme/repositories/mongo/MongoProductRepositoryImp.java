package com.isep.acme.repositories.mongo;

import com.isep.acme.model.Product;
import com.isep.acme.repositories.ProductRepository;
import lombok.SneakyThrows;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("MongoProductRepositoryImp")
public class MongoProductRepositoryImp extends MongoBaseRepository<Product, Long> implements ProductRepository {

    public MongoProductRepositoryImp(final MongoTemplate mongoTemplate) {
        super(mongoTemplate, Product.class);
    }

    @SneakyThrows
    @Override
    public <S extends Product> S save(S entity) {
        final Optional<Product> possibleProductInDb=findBySku(entity.getSku());
        if(possibleProductInDb.isPresent() && !possibleProductInDb.get().getProductID().equals(entity.getProductID())){
            throw new Exception("Sku must be unique");
        }
        return super.save(entity);
    }


    @Override
    public Optional<Product> findBySku(String sku) {
        Query query = new Query();
        query.addCriteria(Criteria.where("sku").is(sku));
        return mongoTemplate.find(query, Product.class).stream().findAny();
    }
}
