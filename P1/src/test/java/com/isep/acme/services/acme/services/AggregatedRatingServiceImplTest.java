package com.isep.acme.services.acme.services;

import com.isep.acme.model.AggregatedRating;
import com.isep.acme.model.Product;
import com.isep.acme.repositories.AggregatedRatingRepository;
import com.isep.acme.repositories.ProductRepository;
import com.isep.acme.services.AggregatedRatingServiceImpl;
import com.isep.acme.services.ReviewServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AggregatedRatingServiceImplTest {
    @Mock
    private AggregatedRatingRepository arRepository;

    @Mock
    private ProductRepository pRepository;

    @Mock
    private ReviewServiceImpl rService;

    @InjectMocks
    private AggregatedRatingServiceImpl aggregatedRatingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        String sku = "123";
        Product product = new Product(1L, "sku");
        when(pRepository.findBySku(eq(sku))).thenReturn(Optional.of(product));

        Double average = 4.5;
        when(rService.getWeightedAverage(product)).thenReturn(average);

        Optional<AggregatedRating> aggregatedRating = Optional.empty();
        when(arRepository.findByProductId(product)).thenReturn(aggregatedRating);
        when(arRepository.save(any(AggregatedRating.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AggregatedRating result = aggregatedRatingService.save(sku);

        verify(pRepository).findBySku(sku);

        verify(rService).getWeightedAverage(product);

        verify(arRepository).findByProductId(product);

        verify(arRepository).save(any(AggregatedRating.class));

        assertEquals(average, result.getAverage());
    }
}
