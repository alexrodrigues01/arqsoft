package com.isep.acme.services;

import com.isep.acme.model.dto.VoteDTO;

import java.util.List;
import java.util.Optional;

public interface VoteService {
    Optional<List<VoteDTO>> getVoteByReview(final Long reviewId);
}
