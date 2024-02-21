package com.isep.acme.services;


import com.isep.acme.model.Product;
import com.isep.acme.model.product.ProductDTO;
import com.isep.acme.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl {

    private ProductRepository repository;


    @Autowired
    public void setProductRepo(@Value("${product.repo}") String bean, ApplicationContext applicationContext){
        repository= (ProductRepository) applicationContext.getBean(bean);
    }

//    @Override
    public void create(final Product product) {
        final Product p = new Product(product.getSku());

        repository.save(p);
    }


//    @Override
    public void deleteBySku(String sku) {
        final Optional<Product> productToDelete = repository.findBySku(sku);

        if (productToDelete.isEmpty()) return;

        repository.delete(productToDelete.get());
    }
}
