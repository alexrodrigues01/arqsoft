package com.isep.acme.services.acme.services;

import com.isep.acme.model.Product;
import com.isep.acme.model.dto.ProductDTO;
import com.isep.acme.model.dto.ProductDetailDTO;
import com.isep.acme.model.dto.ProductWithoutSkuDTO;
import com.isep.acme.repositories.ProductRepository;
import com.isep.acme.services.ProductServiceImpl;
import com.isep.acme.services.SkuGeneration;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
class ProductServiceImplTest {
    @Mock
    private ProductRepository repository;

    @Mock
    private SkuGeneration skuGeneration;

    @Mock
    private ApplicationContext applicationContext;

    @InjectMocks
    private ProductServiceImpl productService;


    @Test
    void testGetProductBySku() {
        String sku = "123";
        Product product = new Product(sku, "Product Name", "Product Description");
        when(repository.findBySku(sku)).thenReturn(Optional.of(product));

        Optional<Product> result = productService.getProductBySku(sku);

        verify(repository).findBySku(sku);
        assertEquals(Optional.of(product), result);
    }

    @Test
    void testFindBySku() {
        String sku = "123";
        Product product = new Product(sku, "Product Name", "Product Description");
        ProductDTO productDTO = new ProductDTO(sku, "Product Name");
        when(repository.findBySku(sku)).thenReturn(Optional.of(product));

        Optional<ProductDTO> result = productService.findBySku(sku);

        verify(repository).findBySku(sku);

        assertEquals(productDTO, result.get());
    }

    @Test
    void testFindByDesignation() {
        String sku = "123";
        String designation = "Product Name";
        List<Product> products = new ArrayList<>();
        Product product = new Product(sku, "Product Name", "Product Description");
        products.add(product);
        when(repository.findByDesignation(designation)).thenReturn(products);

        Iterable<ProductDTO> result = productService.findByDesignation(designation);

        verify(repository).findByDesignation(designation);
        ProductDTO productDTO = new ProductDTO(sku, "Product Name");
        assertTrue(result.iterator().next().equals(productDTO));
    }

    @Test
    void testGetCatalog() {
        String sku = "123";
        List<Product> products = new ArrayList<>();
        Product product = new Product(sku, "Product Name", "Product Description");
        products.add(product);
        when(repository.findAll()).thenReturn(products);

        Iterable<ProductDTO> result = productService.getCatalog();

        verify(repository).findAll();
        ProductDTO productDTO = new ProductDTO(sku, "Product Name");
        assertTrue(result.iterator().next().equals(productDTO));
    }

    @Test
    void testGetDetails() {
        String sku = "123";
        Product product = new Product(sku, "Product Name", "Product Description");
        when(repository.findBySku(sku)).thenReturn(Optional.of(product));

        ProductDetailDTO result = productService.getDetails(sku);

        verify(repository).findBySku(sku);
        ProductDetailDTO productDetailDTO = new ProductDetailDTO(sku, "Product Name", "Product Description");
        assertEquals(productDetailDTO, result);
    }

    @Test
    void testCreate() {
        Product product = new Product("123", "Product Name", "Product Description");
        when(repository.save(eq(product))).thenReturn(product);

        ProductDTO result = productService.create(product);

        verify(repository).save(eq(product));
        ProductDTO productDTO = new ProductDTO("123", "Product Name");

        assertEquals(productDTO, result);
    }

    @Test
    void testCreateWithoutSku() {
        ProductWithoutSkuDTO productWithoutSku = new ProductWithoutSkuDTO("Product Name", "Product Description");
        when(skuGeneration.getSkuAlgorithm(productWithoutSku)).thenReturn("123");
        when(repository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProductDTO result = productService.createWithoutSku(productWithoutSku);

        verify(skuGeneration).getSkuAlgorithm(productWithoutSku);
        verify(repository).save(any(Product.class));
        // Add assertions for the expected result
    }

    @Test
    void testUpdateBySku() {
        String sku = "123";
        Product existingProduct = new Product(sku, "Product Name", "Product Description");
        Product updatedProduct = new Product(sku, "Updated Product Name", "Updated Product Description");
        when(repository.findBySku(sku)).thenReturn(Optional.of(existingProduct));
        when(repository.save(existingProduct)).thenReturn(updatedProduct);

        ProductDTO result = productService.updateBySku(sku, updatedProduct);

        verify(repository).findBySku(sku);
        verify(repository).save(existingProduct);
    }

    @Test
    void testDeleteBySku() {
        String sku = "123";
        doNothing().when(repository).delete(any());

        productService.deleteBySku(sku);

        verify(repository).findBySku(sku);
    }

}
