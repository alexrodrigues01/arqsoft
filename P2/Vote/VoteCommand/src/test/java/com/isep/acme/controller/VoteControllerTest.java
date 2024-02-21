package com.isep.acme.controller;

import com.isep.acme.controllers.VoteController;
import com.isep.acme.model.dto.VoteDTO;
import com.isep.acme.model.dto.VoteQueueDTO;
import com.isep.acme.rabbitmq.Runner;
import com.isep.acme.services.VoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

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
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createVote() throws Exception {
        Long userId = 50L;
        String vote = "downVote";
        Long reviewId = 150L;

        VoteDTO createVoteDTO = new VoteDTO(userId,vote,reviewId);
        VoteQueueDTO expectedVote = new VoteQueueDTO(5L,10L,"upVote",15L);

        when(voteService.create(eq(createVoteDTO))).thenReturn(expectedVote);

        ResponseEntity<VoteQueueDTO> response = voteController.createVote(createVoteDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedVote, response.getBody());
    }

}
