package com.isep.acme.services.neo4J;

import com.isep.acme.model.AggregatedRating;
import com.isep.acme.model.AggregatedRatingNeo4J;
import com.isep.acme.model.Product;
import com.isep.acme.model.ProductNeo4J;
import com.isep.acme.repositories.neo4J.AggregatedRatingRepositoryNeo4J;
import com.isep.acme.repositories.neo4J.ProductRepositoryNeo4j;
import com.isep.acme.services.neo4J.AggregatedRatingServiceNeo4JImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class AggregatedRatingServiceNeo4JImplTest {
    @Mock
    private AggregatedRatingRepositoryNeo4J arRepository;

    @Mock
    private ProductRepositoryNeo4j pRepository;

    @Mock
    private ReviewServiceNeo4JImpl rService;

    @InjectMocks
    private AggregatedRatingServiceNeo4JImpl aggregatedRatingService;

    @Test
    void testSave() {
        String sku = "123";
        ProductNeo4J productNeo4J = new ProductNeo4J(1L, "sku", "designation", "description");
        Product product = new Product(1L, "sku", "designation", "description");
        when(pRepository.findBySku(sku)).thenReturn(Optional.of(productNeo4J));

        Double average = 4.5;
        when(rService.getWeightedAverage(eq(product))).thenReturn(average);

        Optional<AggregatedRatingNeo4J> aggregatedRating = Optional.empty();
        when(arRepository.findByProduct(eq(product.getProductID()))).thenReturn(aggregatedRating);

        when(arRepository.save(any(AggregatedRatingNeo4J.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AggregatedRating result = aggregatedRatingService.save(sku);

        verify(pRepository).findBySku(sku);
        verify(rService).getWeightedAverage(eq(product));
        verify(arRepository).findByProduct(eq(product.getProductID()));
        verify(arRepository).save(any(AggregatedRatingNeo4J.class));

        assertEquals(average, result.getAverage());
    }
}
