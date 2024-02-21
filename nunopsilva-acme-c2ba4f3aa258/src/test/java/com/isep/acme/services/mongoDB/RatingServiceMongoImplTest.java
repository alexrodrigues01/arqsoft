package com.isep.acme.services.mongoDB;

import com.isep.acme.model.Rating;
import com.isep.acme.repositories.mongoDB.RatingRepositoryMongo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class RatingServiceMongoImplTest {

    @Mock
    private RatingRepositoryMongo repository;

    @InjectMocks
    private RatingServiceMongoImpl ratingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testFindByRate() {
        Double rate = 4.5;
        Rating rating = new Rating(rate);
        when(repository.findByRate(rate)).thenReturn(Optional.of(rating));

        Optional<Rating> result = ratingService.findByRate(rate);

        verify(repository).findByRate(rate);
        assertEquals(Optional.of(rating), result);
    }

    @Test
    void testFindByRateNotFound() {
        Double rate = 4.5;
        when(repository.findByRate(rate)).thenReturn(Optional.empty());

        Optional<Rating> result = ratingService.findByRate(rate);

        verify(repository).findByRate(rate);
        assertEquals(Optional.empty(), result);
    }
}
