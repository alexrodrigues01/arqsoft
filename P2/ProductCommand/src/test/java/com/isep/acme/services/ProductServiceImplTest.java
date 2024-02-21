package com.isep.acme.services;

import com.isep.acme.controllers.ResourceNotFoundException;
import com.isep.acme.model.Product;
import com.isep.acme.model.User;
import com.isep.acme.model.dto.ProductDTO;
import com.isep.acme.repositories.ProductRepository;
import com.isep.acme.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testApproveProduct() {

        String sku = "testSku123";
        String userName = "testUser";
        String userName2 = "testUser2";

        Product product = new Product(sku, "Test Product", "Description");
        Optional<Product> optionalProduct = Optional.of(product);
        User user = new User(userName, "password");
        Optional<User> optionalUser = Optional.of(user);

        User user2 = new User(userName2, "password");
        Optional<User> optionalUser2 = Optional.of(user2);


        when(productRepository.findBySku(sku)).thenReturn(optionalProduct);
        when(userRepository.findByUsername(userName)).thenReturn(optionalUser);
        when(userRepository.findByUsername(userName2)).thenReturn(optionalUser2);
        when(productRepository.save(Mockito.any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        productService.approveProduct(sku, userName);
        ProductDTO result = productService.approveProduct(sku,userName2);

        assertNotNull(result);
        assertEquals(sku, result.getSku());
        assertTrue(result.isPublished());
    }

    @Test
    void testApproveProductProductNotFound() {
        // Mock data
        String sku = "nonExistentSku";
        String userName = "testUser";

        // Mock repository methods
        when(productRepository.findBySku(anyString())).thenReturn(Optional.empty());

        // Perform the test and assert exception
        assertThrows(ResourceNotFoundException.class, () -> productService.approveProduct(sku, userName));
    }

    @Test
    void testApproveProductUserNotFound() {
        // Mock data
        String sku = "testSku123";
        String userName = "nonExistentUser";

        Product product = new Product(sku, "Test Product", "Description");
        Optional<Product> optionalProduct = Optional.of(product);

        // Mock repository methods
        when(productRepository.findBySku(sku)).thenReturn(optionalProduct);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // Perform the test and assert exception
        assertThrows(ResourceNotFoundException.class, () -> productService.approveProduct(sku, userName));
    }
}