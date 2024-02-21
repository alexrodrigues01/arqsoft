package com.isep.acme.services.mongoDB;

import com.isep.acme.model.Product;
import com.isep.acme.model.ProductMongo;
import com.isep.acme.model.dto.ProductDTO;
import com.isep.acme.model.dto.ProductDetailDTO;
import com.isep.acme.model.dto.ProductWithoutSkuDTO;
import com.isep.acme.repositories.mongoDB.ProductRepositoryMongo;
import com.isep.acme.services.SkuGeneration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.context.ApplicationContext;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProductServiceMongoImplTest {
    @InjectMocks
    private ProductServiceMongoImpl productServiceMongo;

    @Mock
    private ProductRepositoryMongo repository;

    @Mock
    private SkuGeneration skuGeneration;

    @Mock
    private ApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testGetProductBySku() {
        String sku = "SKU123";
        Product product = new Product(sku, "Test Product", "Description");
        ProductMongo productMongo = new ProductMongo(sku, "Test Product", "Description");
        when(repository.findBySku(sku)).thenReturn(Optional.of(productMongo));

        Optional<Product> result = productServiceMongo.getProductBySku(sku);
        assertTrue(result.isPresent());
        assertEquals(sku, result.get().getSku());
    }

    @Test
    void testFindBySkuWithExistingProduct() {
        String sku = "SKU123";
        Product product = new Product(sku, "Test Product", "Description");
        ProductMongo productMongo = new ProductMongo(sku, "Test Product", "Description");
        when(repository.findBySku(sku)).thenReturn(Optional.of(productMongo));

        Optional<ProductDTO> result = productServiceMongo.findBySku(sku);
        assertTrue(result.isPresent());
        assertEquals(sku, result.get().getSku());
    }

    @Test
    void testFindBySkuWithNonExistingProduct() {
        String sku = "NonExistentSKU";
        when(repository.findBySku(sku)).thenReturn(Optional.empty());

        Optional<ProductDTO> result = productServiceMongo.findBySku(sku);
        assertFalse(result.isPresent());
    }

    @Test
    void testFindByDesignation() {
        String designation = "Test Product";
        List<Product> products = List.of(new Product("SKU1", designation, "Description1"),
                new Product("SKU2", designation, "Description2"));

        List<ProductMongo> productsMongo = List.of(new ProductMongo("SKU1", designation, "Description1"),
                new ProductMongo("SKU2", designation, "Description2"));
        when(repository.findByDesignation(designation)).thenReturn(productsMongo);

        Iterable<ProductDTO> result = productServiceMongo.findByDesignation(designation);
        assertEquals(2, ((List<ProductDTO>) result).size());
    }

    @Test
    void testGetCatalog() {
        List<Product> products = List.of(
                new Product("SKU1", "Product1", "Description1"),
                new Product("SKU2", "Product2", "Description2")
        );

        List<ProductMongo> productsMongo = List.of(
                new ProductMongo("SKU1", "Product1", "Description1"),
                new ProductMongo("SKU2", "Product2", "Description2")
        );

        when(repository.findAll()).thenReturn(productsMongo);

        Iterable<ProductDTO> result = productServiceMongo.getCatalog();
        assertEquals(2, ((List<ProductDTO>) result).size());
    }

    @Test
    void testGetDetailsWithExistingProduct() {
        String sku = "SKU123";
        Product product = new Product(sku, "Test Product", "Description");
        ProductMongo productMongo = new ProductMongo(sku, "Test Product", "Description");
        when(repository.findBySku(sku)).thenReturn(Optional.of(productMongo));

        ProductDetailDTO result = productServiceMongo.getDetails(sku);
        assertNotNull(result);
        assertEquals(sku, result.getSku());
    }

    @Test
    void testGetDetailsWithNonExistingProduct() {
        String sku = "NonExistentSKU";
        when(repository.findBySku(sku)).thenReturn(Optional.empty());

        ProductDetailDTO result = productServiceMongo.getDetails(sku);
        assertNull(result);
    }


    @Test
    void testUpdateBySkuWithNonExistingProduct() {
        String sku = "NonExistentSKU";
        when(repository.findBySku(sku)).thenReturn(Optional.empty());

        Product updatedProduct = new Product(sku, "Updated Product", "Updated Description");
        ProductDTO result = productServiceMongo.updateBySku(sku, updatedProduct);
        assertNull(result);
    }

    @Test
    void testDeleteBySku() {
        String sku = "SKU123";
        productServiceMongo.deleteBySku(sku);
        verify(repository).deleteBySku(sku);
    }
}
