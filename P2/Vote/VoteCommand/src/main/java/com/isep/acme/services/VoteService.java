package com.isep.acme.services;

import com.isep.acme.model.dto.VoteDTO;
import com.isep.acme.model.dto.VoteQueueDTO;

import java.util.List;
import java.util.Optional;

public interface VoteService {

    VoteQueueDTO create(final VoteDTO voteDTO);
}
