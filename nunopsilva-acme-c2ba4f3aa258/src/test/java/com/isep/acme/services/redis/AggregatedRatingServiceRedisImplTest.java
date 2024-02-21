package com.isep.acme.services.redis;

import com.isep.acme.model.AggregatedRating;
import com.isep.acme.model.AggregatedRatingRedis;
import com.isep.acme.model.Product;
import com.isep.acme.model.ProductRedis;
import com.isep.acme.repositories.redis.AggregatedRatingRepositoryRedis;
import com.isep.acme.repositories.redis.ProductRepositoryRedis;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AggregatedRatingServiceRedisImplTest {

    @InjectMocks
    private AggregatedRatingServiceRedisImpl aggregatedRatingService;

    @Mock
    private AggregatedRatingRepositoryRedis arRepository;

    @Mock
    private ProductRepositoryRedis pRepository;

    @Mock
    private ReviewServiceRedisImpl rService;


    @Test
    public void testSave() {

        // Create a sample ProductRedis and AggregatedRatingRedis for testing
        ProductRedis productRedis = new ProductRedis(1L, "123456789012", "Designation", "Description");
        Product product= new Product(productRedis.getProductID(), productRedis.getSku(), productRedis.getDesignation(), productRedis.getDescription());

        AggregatedRatingRedis aggregatedRatingRedis = new AggregatedRatingRedis(0.0, productRedis);

        // Define the behavior of the mock repositories
        when(pRepository.findBySku("123456789012")).thenReturn(Optional.of(productRedis));
        when(arRepository.findByProductId(productRedis)).thenReturn(Optional.of(aggregatedRatingRedis));
        when(rService.getWeightedAverage(eq(product))).thenReturn(0.0);

        // Call the method to be tested
        AggregatedRating result = aggregatedRatingService.save("123456789012");

        // Verify the result
        assertNotNull(result);
        assertEquals(aggregatedRatingRedis.getAverage(), result.getAverage());
        assertEquals(aggregatedRatingRedis.getProduct().getSku(), result.getProduct().getSku());
        // Add more assertions as needed.
    }
}
