package com.isep.acme.controllers;

import com.isep.acme.ACMEApplication;
import com.isep.acme.exceptions.ResourceNotFoundException;
import com.isep.acme.model.EventDTO;
import com.isep.acme.model.TypeOfEvent;
import com.isep.acme.model.review.*;
import com.isep.acme.rabbitmq.Runner;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.isep.acme.services.ReviewService;


@Tag(name = "Review", description = "Endpoints for managing Review")
@RestController
@RequiredArgsConstructor
class ReviewController {

    private final ReviewService rService;

    private final Runner runner;

    @Operation(summary = "creates review")
    @PostMapping("/products/{sku}/reviews")
    public ResponseEntity<ReviewDTO> createReview(@PathVariable(value = "sku") final String sku, @RequestBody CreateReviewDTO createReviewDTO) throws Exception {

        final var review = rService.create(createReviewDTO, sku);

        if (review == null) {
            return ResponseEntity.badRequest().build();
        }

        EventDTO eventDTO = new EventDTO(TypeOfEvent.CREATE, "Review", new ReviewQueueDTO(review.getIdReview(), review.getReviewText(), review.getPublishingDate(), review.getApprovalStatus(), review.getFunFact(), review.getRating(), createReviewDTO.getUserID(), sku, review.getApproves()));
        runner.sendMessage(eventDTO, ACMEApplication.fanoutExchangeName2);
        runner.sendMessage(eventDTO, ACMEApplication.fanOutVote);

        return new ResponseEntity<>(review, HttpStatus.CREATED);
    }

    @Operation(summary = "deletes review")
    @DeleteMapping("/reviews/{reviewID}")
    public ResponseEntity<Boolean> deleteReview(@PathVariable(value = "reviewID") final Long reviewID) throws Exception {

        Boolean rev = rService.deleteReview(reviewID);

        if (rev == null) return ResponseEntity.notFound().build();

        if (!rev) return ResponseEntity.unprocessableEntity().build();

        runner.sendMessage(new EventDTO(TypeOfEvent.DELETE, "Review", new ReviewQueueDTO(reviewID)), ACMEApplication.fanoutExchangeName2);
        runner.sendMessage(new EventDTO(TypeOfEvent.DELETE, "Review", new ReviewQueueDTO(reviewID)), ACMEApplication.fanOutVote);

        return ResponseEntity.ok().body(true);
    }

    @Operation(summary = "Accept or reject review")
    @PutMapping("/reviews/acceptreject/{reviewID}")
    public ResponseEntity<ReviewDTO> putAcceptRejectReview(@PathVariable(value = "reviewID") final Long reviewID, @RequestBody ModerateReviewDTO moderateReviewDTO) {

        try {
            ReviewDTO review = rService.moderateReview(reviewID, moderateReviewDTO.getApproved(), moderateReviewDTO.getUserId());
            EventDTO eventDTO = new EventDTO(TypeOfEvent.UPDATE, "Review", new ReviewQueueDTO(review.getIdReview(), review.getReviewText(), review.getPublishingDate(), review.getApprovalStatus(), review.getFunFact(), review.getRating(), moderateReviewDTO.getUserId(), review.getSku(), review.getApproves()));
            runner.sendMessage(eventDTO, ACMEApplication.fanoutExchangeName2);
            runner.sendMessage(eventDTO, ACMEApplication.fanOutVote);
            return ResponseEntity.ok().body(review);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
