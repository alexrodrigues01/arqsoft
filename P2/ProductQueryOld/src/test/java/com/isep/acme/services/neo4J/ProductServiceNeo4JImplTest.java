package com.isep.acme.services.neo4J;

import com.isep.acme.model.Product;
import com.isep.acme.model.ProductNeo4J;
import com.isep.acme.model.dto.ProductDTO;
import com.isep.acme.model.dto.ProductDetailDTO;
import com.isep.acme.model.dto.ProductWithoutSkuDTO;
import com.isep.acme.repositories.neo4J.ProductRepositoryNeo4j;
import com.isep.acme.services.ProductService;
import com.isep.acme.services.SkuGeneration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProductServiceNeo4JImplTest {

    @InjectMocks
    private ProductServiceNeo4JImpl productService;

    @Mock
    private ProductRepositoryNeo4j repository;

    @Mock
    private SkuGeneration skuGeneration;

    @Test
    public void testGetProductBySku_ProductExists() {
        String sku = "123";
        ProductNeo4J productNeo4J = new ProductNeo4J(1L, sku, "Test Product", "Description");
        when(repository.findBySku(sku)).thenReturn(Optional.of(productNeo4J));

        Optional<Product> result = productService.getProductBySku(sku);

        verify(repository, times(1)).findBySku(sku);
        assertTrue(result.isPresent());
        assertEquals(sku, result.get().getSku());
    }

    @Test
    public void testGetProductBySku_ProductDoesNotExist() {
        String sku = "123";
        ProductNeo4J productNeo4J = new ProductNeo4J(1L, "sku", "designation", "description");
        when(repository.findBySku(sku)).thenReturn(Optional.of(productNeo4J));

        Optional<Product> result = productService.getProductBySku(sku);

        Product product = new Product(1L, "sku", "designation", "description");
        verify(repository, times(1)).findBySku(sku);
        assertEquals(product, result.get());
    }

    @Test
    public void testFindBySku_ProductExists() {
        String sku = "123";
        ProductNeo4J productNeo4J = new ProductNeo4J(1L, sku, "Test Product", "Description");
        when(repository.findBySku(sku)).thenReturn(Optional.of(productNeo4J));

        Optional<ProductDTO> result = productService.findBySku(sku);

        verify(repository, times(1)).findBySku(sku);
        assertTrue(result.isPresent());
        assertEquals(sku, result.get().getSku());
    }

    @Test
    public void testFindBySku_ProductDoesNotExist() {
        String sku = "123";
        when(repository.findBySku(sku)).thenReturn(Optional.empty());

        Optional<ProductDTO> result = productService.findBySku(sku);

        verify(repository, times(1)).findBySku(sku);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindByDesignation() {
        String designation = "Test Designation";
        List<ProductNeo4J> products = new ArrayList<>();
        products.add(new ProductNeo4J(1L, "123", designation, "Description 1"));
        products.add(new ProductNeo4J(2L, "456", designation, "Description 2"));
        when(repository.findByDesignation(designation)).thenReturn(products);

        Iterable<ProductDTO> result = productService.findByDesignation(designation);

        verify(repository, times(1)).findByDesignation(designation);
        List<ProductDTO> resultList = new ArrayList<>();
        result.forEach(productDTO -> resultList.add(productDTO));
        assertEquals(2, resultList.size());
    }

    @Test
    public void testGetCatalog() {
        List<ProductNeo4J> products = new ArrayList<>();
        products.add(new ProductNeo4J(1L, "123", "Product 1", "Description 1"));
        products.add(new ProductNeo4J(2L, "456", "Product 2", "Description 2"));
        when(repository.findAll()).thenReturn(products);

        Iterable<ProductDTO> result = productService.getCatalog();

        verify(repository, times(1)).findAll();
        List<ProductDTO> resultList = new ArrayList<>();
        result.forEach(resultList::add);
        assertEquals(2, resultList.size());
    }

    @Test
    public void testGetDetails_ProductExists() {
        String sku = "123";
        ProductNeo4J productNeo4J = new ProductNeo4J(1L, sku, "Test Product", "Description");
        when(repository.findBySku(sku)).thenReturn(Optional.of(productNeo4J));

        ProductDetailDTO result = productService.getDetails(sku);

        verify(repository, times(1)).findBySku(sku);
        assertNotNull(result);
        assertEquals(sku, result.getSku());
    }

    @Test
    public void testGetDetails_ProductDoesNotExist() {
        String sku = "123";
        when(repository.findBySku(sku)).thenReturn(Optional.empty());

        ProductDetailDTO result = productService.getDetails(sku);

        verify(repository, times(1)).findBySku(sku);
        assertNull(result);
    }

    @Test
    public void testCreate() {
        Product product = new Product("123", "Test Product", "Description");
        ProductNeo4J productNeo4J = new ProductNeo4J(1L, "123", "Test Product", "Description");
        when(repository.save(any(ProductNeo4J.class))).thenReturn(productNeo4J);

        ProductDTO result = productService.create(product);

        verify(repository, times(1)).save(any(ProductNeo4J.class));
        assertNotNull(result);
        assertEquals("123", result.getSku());
        assertEquals("Test Product", result.getDesignation());
    }

    @Test
    public void testCreateWithoutSku() {
        String generatedSku = "456";
        ProductWithoutSkuDTO productWithoutSku = new ProductWithoutSkuDTO("Test Product", "Description");
        ProductNeo4J productNeo4J = new ProductNeo4J(1L, generatedSku, "Test Product", "Description");
        when(skuGeneration.getSkuAlgorithm(productWithoutSku)).thenReturn(generatedSku);
        when(repository.save(any(ProductNeo4J.class))).thenReturn(productNeo4J);

        ProductDTO result = productService.createWithoutSku(productWithoutSku);

        verify(skuGeneration, times(1)).getSkuAlgorithm(productWithoutSku);
        verify(repository, times(1)).save(any(ProductNeo4J.class));
        assertNotNull(result);
        assertEquals(generatedSku, result.getSku());
        assertEquals("Test Product", result.getDesignation());
    }

    @Test
    public void testUpdateBySku_ProductExists() {
        String sku = "123";
        Product product = new Product(1L,"123", "Updated Product", "Updated Description");
        ProductNeo4J productNeo4J = new ProductNeo4J(1L, sku, "Updated Product", "Updated Description");
        when(repository.findBySku(sku)).thenReturn(Optional.of(productNeo4J));
        when(repository.save(any(ProductNeo4J.class))).thenReturn(productNeo4J);

        ProductDTO result = productService.updateBySku(sku, product);

        verify(repository, times(1)).findBySku(sku);
        verify(repository, times(1)).save(any(ProductNeo4J.class));
        assertNotNull(result);
        assertEquals(sku, result.getSku());
        assertEquals("Updated Product", result.getDesignation());
    }

    @Test
    public void testUpdateBySku_ProductDoesNotExist() {
        String sku = "123";
        Product product = new Product(1L,"123", "Updated Product", "Updated Description");
        when(repository.findBySku(sku)).thenReturn(Optional.empty());

        ProductDTO result = productService.updateBySku(sku, product);

        verify(repository, times(1)).findBySku(sku);
        verify(repository, never()).save(any(ProductNeo4J.class));
        assertNull(result);
    }

    @Test
    public void testDeleteBySku() {
        String sku = "123";

        productService.deleteBySku(sku);

        verify(repository, times(1)).deleteBySku(sku);
    }
}