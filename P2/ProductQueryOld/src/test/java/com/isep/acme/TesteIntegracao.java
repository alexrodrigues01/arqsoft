package com.isep.acme;

import com.isep.acme.model.Product;
import com.isep.acme.model.User;
import com.isep.acme.model.dto.ProductDTO;

import com.isep.acme.repositories.h2.UserRepository;
import com.isep.acme.services.h2.ProductServiceImpl;
import com.isep.acme.services.h2.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;


@SpringBootTest
public class TesteIntegracao {

    @Autowired
    private ProductServiceImpl productService;



    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;




    @Test
    void testCreate() {


        Product product = new Product("123", "Product Name", "Product Description");


        ProductDTO result = productService.create(product);

        Optional<ProductDTO> productFound=  productService.findBySku(product.getSku());

        assertTrue(productFound.isPresent());
        assertEquals(product.getDesignation(),productFound.get().getDesignation());
        assertEquals(product.sku,productFound.get().getSku());
    }


}
