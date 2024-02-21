package com.isep.acme.services;

import com.isep.acme.model.Review;
import com.isep.acme.model.User;
import com.isep.acme.model.Vote;
import com.isep.acme.model.dto.VoteDTO;
import com.isep.acme.model.dto.VoteQueueDTO;
import com.isep.acme.repositories.ReviewRepository;
import com.isep.acme.repositories.UserRepository;
import com.isep.acme.repositories.VoteRepository;
import com.isep.acme.repositories.mongoDB.VoteRepositoryMongo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class VoteServiceTest {

    @InjectMocks
    private VoteServiceImpl voteService;

    @Mock
    private VoteRepository voteRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createVote() {
        Long userId = 50L;
        String voteText = "downVote";
        Long reviewId = 150L;

        VoteDTO createVoteDTO = new VoteDTO(userId, voteText, reviewId);

        User user = new User(userId);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(new Review()));
        when(userService.getUserId(userId)).thenReturn(Optional.of(user));
        when(voteRepository.findVote(any())).thenReturn(false);

        Vote vote = new Vote(1L,5L,"upVote",10L);
        when(voteRepository.save(any())).thenReturn(vote);

        VoteQueueDTO result = voteService.create(createVoteDTO);

        verify(reviewRepository).findById(reviewId);
        verify(voteRepository).findVote(any());
        verify(voteRepository).save(any());
    }

}