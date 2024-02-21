package com.isep.acme.controllers;

import com.isep.acme.model.Product;
import com.isep.acme.model.dto.ProductDTO;
import com.isep.acme.model.dto.ProductWithoutSkuDTO;
import com.isep.acme.services.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
class ProductControllerTest {

    @InjectMocks
    ProductController productController;

    @Mock
    ProductService service;


    @Test
    void getCatalog() {
        List<ProductDTO> expectedProducts = new ArrayList<>();

        when(service.getCatalog()).thenReturn(expectedProducts);

        ResponseEntity<Iterable<ProductDTO>> response = productController.getCatalog();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedProducts, response.getBody());
    }

    @Test
    void getProductBySku() {
        String sku = "SKU123";
        ProductDTO expectedProduct = new ProductDTO(sku,"Designação");

        when(service.findBySku(eq(sku))).thenReturn(Optional.of(expectedProduct));

        ResponseEntity<ProductDTO> response = productController.getProductBySku(sku);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedProduct, response.getBody());
    }

    @Test
    void getProductBySku_ProductNotFound() {
        String sku = "SKU123";

        when(service.findBySku(eq(sku))).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> productController.getProductBySku(sku));
    }

    @Test
    void findAllByDesignation() {
        String designation = "Sample Designation";
        List<ProductDTO> expectedProducts = new ArrayList<>();

        when(service.findByDesignation(eq(designation))).thenReturn(expectedProducts);

        ResponseEntity<Iterable<ProductDTO>> response = productController.findAllByDesignation(designation);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedProducts, response.getBody());
    }

    @Test
    void create() {
        String designation = "Designação";
        Product product = new Product("SKU",designation,"Descrição");
        ProductDTO expectedProductDTO = new ProductDTO("SKU",designation);

        when(service.create(eq(product))).thenReturn(expectedProductDTO);

        ResponseEntity<ProductDTO> response = productController.create(product);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedProductDTO, response.getBody());
    }

    @Test
    void create_ProductConflict() {
        Product product = new Product("SKU","Designação","Descrição");

        when(service.create(eq(product))).thenThrow(new RuntimeException("Product must have a unique SKU"));

        assertThrows(ResponseStatusException.class, () -> productController.create(product));
    }

    @Test
    void createWithoutSku() {
        ProductWithoutSkuDTO productWithNoSku = new ProductWithoutSkuDTO("designação","descrição");
        ProductDTO expectedProductDTO = new ProductDTO("Sku","designação");

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
        String sku = "SKU123";
        Product product = new Product("SKU","Designação","Descrição");
        ProductDTO expectedProductDTO = new ProductDTO(sku,"Designação");

        when(service.updateBySku(eq(sku), eq(product))).thenReturn(expectedProductDTO);

        ResponseEntity<ProductDTO> response = productController.Update(sku, product);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedProductDTO, response.getBody());
    }

    @Test
    void Update_ProductNotFound() {
        String sku = "SKU123";
        Product product = new Product(sku,"Designação","Descrição");

        when(service.updateBySku(eq(sku), eq(product))).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> productController.Update(sku, product));
    }

    @Test
    void delete() {
        String sku = "SKU123";

        doNothing().when(service).deleteBySku(eq(sku));

        ResponseEntity<Product> response = productController.delete(sku);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
