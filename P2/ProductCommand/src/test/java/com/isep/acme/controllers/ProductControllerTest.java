package com.isep.acme.controllers;

import com.isep.acme.model.Product;
import com.isep.acme.model.dto.ProductDTO;
import com.isep.acme.model.dto.ProductWithoutSkuDTO;
import com.isep.acme.rabbitmq.Runner;
import com.isep.acme.services.ProductService;
import com.isep.acme.services.ProductServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class ProductControllerTest {

    @InjectMocks
    ProductController productController;


    @Mock
    private Runner runner;

    @Mock
    ProductServiceImpl service;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void create() {
        String designation = "Designação";
        Product product = new Product("SKU12345",designation,"Descrição");
        ProductDTO expectedProductDTO = new ProductDTO("SKU12345",designation,product.isPublished());

        when(service.create(eq(product))).thenReturn(expectedProductDTO);

        ResponseEntity<ProductDTO> response = productController.create(product);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedProductDTO, response.getBody());
    }

    @Test
    void create_ProductConflict() {
        Product product = new Product("SKU12345","Designação","Descrição");

        when(service.create(eq(product))).thenThrow(new RuntimeException("Product must have a unique SKU"));

        assertThrows(ResponseStatusException.class, () -> productController.create(product));
    }

    @Test
    void createWithoutSku() {
        ProductWithoutSkuDTO productWithNoSku = new ProductWithoutSkuDTO("designação","descrição");
        ProductDTO expectedProductDTO = new ProductDTO("SKU12345","designação",false);

        when(service.createWithoutSku(eq(productWithNoSku))).thenReturn(expectedProductDTO);

        ResponseEntity<ProductDTO> response = productController.createWithoutSku(productWithNoSku);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedProductDTO, response.getBody());
    }

    @Test
    void createWithoutSku_ProductConflict() {
        ProductWithoutSkuDTO productWithNoSku = new ProductWithoutSkuDTO("designação","descrição");

        when(service.createWithoutSku(eq(productWithNoSku))).thenThrow(new RuntimeException("Product must have a unique SKU"));

        assertThrows(ResponseStatusException.class, () -> productController.createWithoutSku(productWithNoSku));
    }

    @Test
    void Update() {
        String sku = "SKU12345";
        Product product = new Product("SKU12345","Designação","Descrição");
        ProductDTO expectedProductDTO = new ProductDTO(sku,"Designação",false);

        when(service.updateBySku(eq(sku), eq(product))).thenReturn(expectedProductDTO);

        ResponseEntity<ProductDTO> response = productController.Update(sku, product);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedProductDTO, response.getBody());
    }

    @Test
    void Update_ProductNotFound() {
        String sku = "SKU12345";
        Product product = new Product(sku,"Designação","Descrição");

        when(service.updateBySku(eq(sku), eq(product))).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> productController.Update(sku, product));
    }

    @Test
    void delete() {
        String sku = "SKU12345";

        doNothing().when(service).deleteBySku(eq(sku));

        ResponseEntity<Product> response = productController.delete(sku);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testApproveProduct() {
        // Mock data
        String sku = "testSku";
        String userName = "testUser";

        ProductDTO productDTO = new ProductDTO("testSku", "Test Product", true);

        // Mock service method
        when(service.approveProduct(sku, userName)).thenReturn(productDTO);

        // Perform the test
        ResponseEntity<ProductDTO> responseEntity = productController.Update(sku, userName);

        // Assertions
        Assertions.assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(productDTO, responseEntity.getBody());
    }

    @Test
    void testApproveProductNotFound() {
        // Mock data
        String sku = "nonExistentSku";
        String userName = "testUser";

        // Mock service method to throw ResourceNotFoundException
        when(service.approveProduct(anyString(), anyString()))
                .thenThrow(new ResourceNotFoundException("Product not found."));

        // Perform the test and assert exception
        assertThrows(ResponseStatusException.class, () -> productController.Update(sku, userName));
    }
}
