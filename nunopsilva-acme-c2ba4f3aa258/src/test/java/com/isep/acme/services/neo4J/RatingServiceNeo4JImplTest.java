package com.isep.acme.services.neo4J;

import com.isep.acme.model.Rating;
import com.isep.acme.model.RatingMapper;
import com.isep.acme.model.RatingNeo4J;
import com.isep.acme.repositories.neo4J.RatingRepositoryNeo4J;
import com.isep.acme.services.RatingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RatingServiceNeo4JImplTest {

    @InjectMocks
    private RatingServiceNeo4JImpl ratingService;

    @Mock
    private RatingRepositoryNeo4J repository;

    @Mock
    private RatingMapper ratingMapper;

    @Test
    public void testFindByRate_RatingExists() {
        double rate = 4.5;
        RatingNeo4J ratingNeo4J = new RatingNeo4J(1L, 1, 4.5);
        when(repository.findByRate(rate)).thenReturn(Optional.of(ratingNeo4J));

        Rating rating = new Rating(1L, 1, 4.5);
        when(ratingMapper.toRating(ratingNeo4J)).thenReturn(rating);

        Optional<Rating> result = ratingService.findByRate(rate);

        verify(repository, times(1)).findByRate(rate);
        verify(ratingMapper, times(1)).toRating(ratingNeo4J);
        assertEquals(4.5, result.get().getRate());
    }

    @Test
    public void testFindByRate_RatingDoesNotExist() {
        double rate = 4.5;
        when(repository.findByRate(rate)).thenReturn(Optional.empty());

        Optional<Rating> result = ratingService.findByRate(rate);

        verify(repository, times(1)).findByRate(rate);
        verify(ratingMapper, never()).toRating(any(RatingNeo4J.class));
        assertTrue(result.isEmpty());
    }
}
