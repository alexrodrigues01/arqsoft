package com.isep.acme.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.isep.acme.model.dto.VoteDTO;
import com.isep.acme.rabbitmq.EventDTO;
import com.isep.acme.rabbitmq.Runner;
import com.isep.acme.rabbitmq.TypeOfEvent;
import com.isep.acme.services.VoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Vote", description = "Endpoints for managing Vote")
@RestController
public class VoteController {

    @Autowired
    private Runner runner;
    @Autowired
    private VoteService service;

    @Operation(summary = "Get Vote By Review Id")
    @GetMapping("/votes/{reviewId}")
    public ResponseEntity<List<VoteDTO>> getVoteByReview(@PathVariable(value = "reviewId") final Long reviewId) throws JsonProcessingException {

        Optional<List<VoteDTO>> v = service.getVoteByReview(reviewId);

        runner.sendMessage(new EventDTO(TypeOfEvent.GET,"Vote", v));

        return ResponseEntity.ok().body(v.get());
    }
}
