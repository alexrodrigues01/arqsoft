package com.isep.acme.services.redis;

import com.isep.acme.model.Rating;
import com.isep.acme.model.RatingRedis;
import com.isep.acme.repositories.redis.RatingRepositoryRedis;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RatingServiceRedisImplTest {

    @InjectMocks
    private RatingServiceRedisImpl ratingService;

    @Mock
    private RatingRepositoryRedis repository;

    @Test
    public void testFindByRate() {
        Double rate = 4.5;

        RatingRedis ratingRedis = new RatingRedis(rate);

        when(repository.findByRate(rate)).thenReturn(Optional.of(ratingRedis));

        Optional<Rating> result = ratingService.findByRate(rate);

        assertTrue(result.isPresent());
        assertEquals(rate, result.get().getRate());
    }

    @Test
    public void testFindByRateNotFound() {
        Double rate = 3.0;

        when(repository.findByRate(rate)).thenReturn(Optional.empty());

        Optional<Rating> result = ratingService.findByRate(rate);

        assertTrue(result.isEmpty());
    }
}
