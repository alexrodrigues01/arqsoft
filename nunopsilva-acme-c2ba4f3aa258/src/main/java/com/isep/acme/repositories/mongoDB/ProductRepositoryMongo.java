package com.isep.acme.repositories.mongoDB;

import com.isep.acme.controllers.FileController;
import com.isep.acme.model.Product;
import com.isep.acme.model.ProductMongo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepositoryMongo extends MongoRepository<ProductMongo,String> {
    Logger logger = LoggerFactory.getLogger(FileController.class);

    Optional<ProductMongo> findBySku(String sku);
    List<ProductMongo> findByDesignation(String designation);

//    @Override
//    default  <S extends ProductMongo> S save(S entity){
//        final Optional<ProductMongo> optionalProduct = findBySku(entity.getSku());
//        if (optionalProduct.isEmpty()){
//            return insert(entity);
//        }
//
//        logger.warn("Product already exists");
//        return null;
//    }

    default <S extends ProductMongo> S updateProduct(S entity){
        return insert(entity);
    }


    void deleteBySku(String sku);
}

