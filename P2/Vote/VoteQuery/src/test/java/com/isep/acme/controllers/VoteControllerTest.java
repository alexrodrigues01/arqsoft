package com.isep.acme.controllers;

import com.isep.acme.model.dto.VoteDTO;
import com.isep.acme.rabbitmq.Runner;
import com.isep.acme.services.VoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class VoteControllerTest {

    @InjectMocks
    VoteController voteController;

    @Mock
    Runner runner;

    @Mock
    VoteService voteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getVoteByReview() throws Exception {
        Long reviewId = 10L;
        List<VoteDTO> expectedVote = new ArrayList<>();


        when(voteService.getVoteByReview(eq(reviewId))).thenReturn(Optional.of(expectedVote));

        ResponseEntity<List<VoteDTO>> response = voteController.getVoteByReview(reviewId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedVote, response.getBody());
    }

}
