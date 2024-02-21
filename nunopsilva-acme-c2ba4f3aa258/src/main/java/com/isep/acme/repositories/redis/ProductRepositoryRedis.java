package com.isep.acme.repositories.redis;


import com.isep.acme.model.ProductRedis;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepositoryRedis extends CrudRepository<ProductRedis, String> {


    List<ProductRedis> findByDesignation(String designation);

    Optional<ProductRedis> findBySku(String sku);

    //Obtain the catalog of products -> Catalog: show sku and designation of all products
    @Query("SELECT p FROM ProductRedis p WHERE p.sku=:sku AND p.description=:description")
    Optional<ProductRedis> getCatalog();



    //Update the product when given the SKU
    @Transactional
    @Modifying
    @Query("UPDATE ProductRedis p SET p.sku = :sku WHERE p.sku=:sku")
    ProductRedis updateBySku(@Param("sku") String sku);

    @Query("SELECT p FROM ProductRedis p WHERE p.productID=:productID")
    Optional<ProductRedis> findById(Long productID);
}
