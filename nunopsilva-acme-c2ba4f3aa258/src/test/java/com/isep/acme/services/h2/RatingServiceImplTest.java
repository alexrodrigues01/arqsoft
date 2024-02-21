package com.isep.acme.services.h2;

import com.isep.acme.model.Rating;
import com.isep.acme.repositories.h2.RatingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RatingServiceImplTest {
    @Mock
    private RatingRepository repository;

    @InjectMocks
    private RatingServiceImpl ratingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testFindByRate() {
        Double rate = 4.5; // Set the rate you want to test
        Rating rating = new Rating(rate);
        when(repository.findByRate(rate)).thenReturn(Optional.of(rating));

        Optional<Rating> result = ratingService.findByRate(rate);

        verify(repository).findByRate(rate);
        assertEquals(Optional.of(rating), result);
    }
}
