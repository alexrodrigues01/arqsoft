package com.isep.acme.services.acme.services;

import com.isep.acme.model.Rating;
import com.isep.acme.repositories.RatingRepository;
import com.isep.acme.services.RatingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
