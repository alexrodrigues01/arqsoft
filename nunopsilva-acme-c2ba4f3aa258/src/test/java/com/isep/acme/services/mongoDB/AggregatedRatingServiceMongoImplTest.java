package com.isep.acme.services.mongoDB;

import com.isep.acme.model.AggregatedRating;
import com.isep.acme.model.Product;
import com.isep.acme.model.ProductMongo;
import com.isep.acme.repositories.mongoDB.AggregatedRatingRepositoryMongo;
import com.isep.acme.repositories.mongoDB.ProductRepositoryMongo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

public class AggregatedRatingServiceMongoImplTest {

    @Mock
    private AggregatedRatingRepositoryMongo arRepository;

    @Mock
    private ProductRepositoryMongo pRepository;

    @Mock
    private ReviewServiceMongoImpl rService;

    @Mock
    private ProductServiceMongoImpl productService;

    @InjectMocks
    private AggregatedRatingServiceMongoImpl aggregatedRatingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testSaveNewAggregatedRating() {
        String sku = "123";
        ProductMongo productMongo = new ProductMongo(1L, sku, "Product Name", "Product Description");
        Product product = new Product(1L, sku, "Product Name", "Product Description");
        when(pRepository.findBySku(sku)).thenReturn(Optional.of(productMongo));
        when(rService.getWeightedAverage(product)).thenReturn(4.0);

        when(arRepository.findByProduct(product)).thenReturn(Optional.empty());

        AggregatedRating newAggregatedRating = new AggregatedRating(4.0, product);
        when(arRepository.save(eq(newAggregatedRating))).thenReturn(newAggregatedRating);

        AggregatedRating result = aggregatedRatingService.save(sku);

        verify(pRepository).findBySku(sku);
        verify(rService).getWeightedAverage(eq(product));
        verify(arRepository).findByProduct(eq(product));
        verify(arRepository).save(eq(newAggregatedRating));
        assertEquals(newAggregatedRating, result);
    }

    @Test
    void testSaveProductNotFound() {
        String sku = "123";
        when(pRepository.findBySku(sku)).thenReturn(Optional.empty());

        AggregatedRating result = aggregatedRatingService.save(sku);

        verify(pRepository).findBySku(sku);
        verify(rService, never()).getWeightedAverage(any());
        verify(arRepository, never()).findByProduct(any());
        verify(arRepository, never()).save(any());
        assertEquals(null, result);
    }
}
