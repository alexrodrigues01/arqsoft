package com.isep.acme.services.redis;

import com.isep.acme.model.Product;
import com.isep.acme.model.ProductRedis;
import com.isep.acme.model.dto.ProductDTO;
import com.isep.acme.model.dto.ProductDetailDTO;
import com.isep.acme.model.dto.ProductWithoutSkuDTO;
import com.isep.acme.repositories.redis.ProductRepositoryRedis;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ProductServiceRedisImplTest {

    @InjectMocks
    private ProductServiceRedisImpl productService;

    @Mock
    private ProductRepositoryRedis repository;

    @Mock
    private ApplicationContext applicationContext;

    @BeforeEach
    public void setup() {
    }

    @Test
    public void testGetProductBySku() {
        String sku = "123456123456";
        ProductRedis productRedis = new ProductRedis(1L, sku, "Sample Product", "Sample Description");
        when(repository.findBySku(sku)).thenReturn(Optional.of(productRedis));
        Optional<Product> result = productService.getProductBySku(sku);
        assertTrue(result.isPresent());
        assertEquals(productRedis.getProductID(), result.get().getProductID());
        assertEquals(productRedis.getSku(), result.get().getSku());
        assertEquals(productRedis.getDesignation(), result.get().getDesignation());
        assertEquals(productRedis.getDescription(), result.get().getDescription());
    }

    @Test
    public void testGetProductBySkuNotFound() {
        String sku = "123456123456";
        when(repository.findBySku(sku)).thenReturn(Optional.empty());
        Optional<Product> result = productService.getProductBySku(sku);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindBySku() {
        String sku = "123456123456";
        ProductRedis productRedis = new ProductRedis(1L, sku, "Sample Product", "Sample Description");
        when(repository.findBySku(sku)).thenReturn(Optional.of(productRedis));
        Optional<ProductDTO> result = productService.findBySku(sku);
        assertTrue(result.isPresent());
        assertEquals(productRedis.toDto(), result.get());
    }

    @Test
    public void testFindBySkuNotFound() {
        String sku = "123456123456";
        when(repository.findBySku(sku)).thenReturn(Optional.empty());
        Optional<ProductDTO> result = productService.findBySku(sku);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindByDesignation() {
        String designation = "Sample Designation";
        List<ProductRedis> productRedisList = new ArrayList<>();
        productRedisList.add(new ProductRedis(1L, "123456123457", designation, "Description 1"));
        productRedisList.add(new ProductRedis(2L, "123456123456", designation, "Description 2"));
        when(repository.findByDesignation(designation)).thenReturn(productRedisList);
        Iterable<ProductDTO> result = productService.findByDesignation(designation);
        List<ProductDTO> resultList = new ArrayList<>();
        for (ProductRedis productRedis : productRedisList) {
            resultList.add(productRedis.toDto());
        }
        assertTrue(result.iterator().hasNext());
        assertEquals(resultList, result);
    }

    @Test
    public void testGetCatalog() {
        List<ProductRedis> productRedisList = new ArrayList<>();
        productRedisList.add(new ProductRedis(1L, "123456123456", "Product 1", "Description 1"));
        productRedisList.add(new ProductRedis(2L, "123456123457", "Product 2", "Description 2"));
        when(repository.findAll()).thenReturn(productRedisList);
        Iterable<ProductDTO> result = productService.getCatalog();
        List<ProductDTO> resultList = new ArrayList<>();
        for (ProductRedis productRedis : productRedisList) {
            resultList.add(productRedis.toDto());
        }
        assertTrue(result.iterator().hasNext());
        assertEquals(resultList, result);
    }

    @Test
    public void testGetDetails() {
        String sku = "123456123456";
        ProductRedis productRedis = new ProductRedis(1L, sku, "Sample Product", "Sample Description");
        when(repository.findBySku(sku)).thenReturn(Optional.of(productRedis));
        ProductDetailDTO result = productService.getDetails(sku);
        assertNotNull(result);
        assertEquals(productRedis.getSku(), result.getSku());
        assertEquals(productRedis.getDesignation(), result.getDesignation());
        assertEquals(productRedis.getDescription(), result.getDescription());
    }

    @Test
    public void testGetDetailsNotFound() {
        String sku = "123456123456";
        when(repository.findBySku(sku)).thenReturn(Optional.empty());
        ProductDetailDTO result = productService.getDetails(sku);
        assertNull(result);
    }

    @Test
    public void testCreate() {
        Product product = new Product(1L, "123456123457", "Sample Product", "Sample Description");
        ProductRedis productRedis = new ProductRedis( "123456123457", "Sample Product", "Sample Description");
        when(repository.save(eq(productRedis))).thenReturn(productRedis);
        ProductDTO result = productService.create(product);
        assertNotNull(result);
        assertEquals(product.toDto(), result);
    }

    @Test
    public void testUpdateBySku() {
        String sku = "123456123456";
        Product product = new Product(1L, sku, "Updated Product", "Updated Description");
        ProductRedis productRedis = new ProductRedis(1L, sku, "Sample Product", "Sample Description");
        when(repository.findBySku(sku)).thenReturn(Optional.of(productRedis));
        when(repository.save(productRedis)).thenReturn(productRedis);
        ProductDTO result = productService.updateBySku(sku, product);
        assertNotNull(result);
        assertEquals(product.toDto(), result);
    }

    @Test
    public void testUpdateBySkuNotFound() {
        String sku = "123456123456";
        Product product = new Product(1L, sku, "Updated Product", "Updated Description");
        when(repository.findBySku(sku)).thenReturn(Optional.empty());
        ProductDTO result = productService.updateBySku(sku, product);
        assertNull(result);
    }

    @Test
    public void testDeleteBySku() {
        String sku = "123456123456";
        ProductRedis productRedis = new ProductRedis(1L, sku, "Sample Product", "Sample Description");
        when(repository.findBySku(sku)).thenReturn(Optional.of(productRedis));
        productService.deleteBySku(sku);
        Mockito.verify(repository, Mockito.times(1)).delete(productRedis);
    }
}
