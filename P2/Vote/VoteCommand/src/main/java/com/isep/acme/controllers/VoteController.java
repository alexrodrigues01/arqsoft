package com.isep.acme.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.isep.acme.ACMEApplication;
import com.isep.acme.model.dto.VoteQueueDTO;
import com.isep.acme.rabbitmq.EventDTO;
import com.isep.acme.rabbitmq.Runner;
import com.isep.acme.rabbitmq.TypeOfEvent;
import com.isep.acme.services.VoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.isep.acme.model.dto.VoteDTO;

@Tag(name = "Vote", description = "Endpoints for managing Vote")
@RestController
public class VoteController {

    @Autowired
    private Runner runner;
    @Autowired
    private VoteService service;

    @Operation(summary = "creates vote")
    @PostMapping("/votes")
    public ResponseEntity<VoteQueueDTO> createVote(@RequestBody VoteDTO voteDTO) throws JsonProcessingException {
        final var vote = service.create(voteDTO);

        if(vote == null){
            return ResponseEntity.badRequest().build();
        }

        runner.sendMessage(new EventDTO(TypeOfEvent.CREATE, "Vote", new VoteQueueDTO(vote.getVoteId(), vote.getUserId(), vote.getVote(), vote.getReviewId())), ACMEApplication.fanoutExchangeName2);
        runner.sendMessage(new EventDTO(TypeOfEvent.CREATE, "Vote", new VoteQueueDTO(vote.getVoteId(), vote.getUserId(), vote.getVote(), vote.getReviewId())), ACMEApplication.fanoutExchangeName);

        return new ResponseEntity<>(vote, HttpStatus.CREATED);
    }
}
