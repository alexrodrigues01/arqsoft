package com.isep.acme.services;

import com.isep.acme.model.Review;
import com.isep.acme.model.Vote;
import com.isep.acme.model.dto.VoteDTO;
import com.isep.acme.repositories.ReviewRepository;
import com.isep.acme.repositories.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("VoteServiceImpl")
@RequiredArgsConstructor
public class VoteServiceImpl implements VoteService {

    private VoteRepository vRepo;

    private ReviewRepository rRepo;

    @Autowired
    public void setVoteRepo(@Value("${vote.repo}") String bean, ApplicationContext applicationContext){
        vRepo= (VoteRepository) applicationContext.getBean(bean);
    }

    @Autowired
    public void setVoteReviewRepo(@Value("${review.repo}") String bean, ApplicationContext applicationContext){
        rRepo= (ReviewRepository) applicationContext.getBean(bean);
    }

    @Override
    public Optional<List<VoteDTO>> getVoteByReview(final Long reviewId) {

        Optional<Review> review = this.rRepo.findById(reviewId);

        if (review.isEmpty()){
            return null;
        }

        Optional<List<Vote>> listVote = vRepo.getVoteByReview(reviewId);

        List<VoteDTO> listVoteDTO = new ArrayList<>();

        List<Vote> list = listVote.get();

        for (Vote v: list) {
            VoteDTO vDTO = v.toDto();
            listVoteDTO.add(vDTO);
        }

        return Optional.of(listVoteDTO);
    }

}
