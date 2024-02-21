package com.isep.acme.controllers;
import com.isep.acme.model.AggregatedRating;
import com.isep.acme.model.Product;
import com.isep.acme.services.AggregatedRatingService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
class AggregatedRatingControllerTest {

    @InjectMocks
    AggregatedRatingController aggregatedRatingController;

    @Mock
    AggregatedRatingService aggregatedRatingService;

    @Test
    void getAverage() {
        String sku = "sampleSku";
        AggregatedRating expectedAggregatedRating = new AggregatedRating(2,new Product(sku,"designação","descrição"));

        when(aggregatedRatingService.save(eq(sku))).thenReturn(expectedAggregatedRating);

        ResponseEntity<AggregatedRating> actualResponse = aggregatedRatingController.getAverage(sku);

        assertEquals(200, actualResponse.getStatusCodeValue());
        assertEquals(expectedAggregatedRating, actualResponse.getBody());
    }
}

