package com.isep.acme.repositories;


import com.isep.acme.model.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends CrudRepository<Product, Long> {
    List<Product> findByDesignation(String designation);
    Optional<Product> findBySku(String sku);
}

