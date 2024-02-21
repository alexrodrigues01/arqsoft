package com.isep.acme.services;

import com.isep.acme.model.Review;
import com.isep.acme.model.Vote;
import com.isep.acme.model.dto.VoteDTO;
import com.isep.acme.repositories.ReviewRepository;
import com.isep.acme.repositories.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class VoteServiceTest {

    @InjectMocks
    VoteServiceImpl voteService;

    @Mock
    VoteRepository voteRepository;

    @Mock
    ReviewRepository reviewRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getVoteByReview() {
        Long reviewId = 10L;
        Optional<Review> expectedReview = Optional.of(new Review());
        Optional<List<Vote>> expectedVotes = Optional.of(new ArrayList<>());

        when(reviewRepository.findById(eq(reviewId))).thenReturn(expectedReview);
        when(voteRepository.getVoteByReview(eq(reviewId))).thenReturn(expectedVotes);

        Optional<List<VoteDTO>> result = voteService.getVoteByReview(reviewId);

        verify(reviewRepository).findById(reviewId);
        verify(voteRepository).getVoteByReview(reviewId);
        assertEquals(expectedVotes, result);
    }

}
