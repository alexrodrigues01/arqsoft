package com.isep.acme.services;

import com.isep.acme.model.Review;
import com.isep.acme.model.Vote;
import com.isep.acme.model.dto.VoteDTO;
import com.isep.acme.model.dto.VoteQueueDTO;
import com.isep.acme.repositories.ReviewRepository;
import com.isep.acme.repositories.UserRepository;
import com.isep.acme.repositories.VoteRepository;
import com.isep.acme.repositories.mongoDB.ReviewRepositoryMongo;
import com.isep.acme.repositories.mongoDB.UserRepositoryMongo;
import com.isep.acme.repositories.mongoDB.VoteRepositoryMongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("VoteServiceImpl")
public class VoteServiceImpl implements VoteService {

    private VoteRepository vRepo;

    private ReviewRepository rRepo;

    @Autowired
    private UserService userService;

    @Autowired
    public void setVoteRepo(@Value("${vote.repo}") String bean, ApplicationContext applicationContext){
        vRepo= (VoteRepository) applicationContext.getBean(bean);
    }

    @Autowired
    public void setVoteReviewRepo(@Value("${review.repo}") String bean, ApplicationContext applicationContext){
        rRepo= (ReviewRepository) applicationContext.getBean(bean);
    }

    public void setVoteRepo(VoteRepositoryMongo repository) {
        this.vRepo = repository;
    }


    public void setVoteReviewRepo(ReviewRepositoryMongo reviewRepository) {
        this.rRepo = reviewRepository;
    }

    public void setVoteUserService(UserService repository) {
        this.userService = repository;
    }

    @Override
    public VoteQueueDTO create(final VoteDTO voteDTO) {

        final Optional<Review> review = rRepo.findById(voteDTO.getReviewId());

        if (review.isEmpty()) return null;

        final var user = userService.getUserId(voteDTO.getUserId());

        if (user.isEmpty()) return null;

        Vote vote = new Vote(voteDTO.getUserId(), voteDTO.getVote(), voteDTO.getReviewId());
        if (voteDTO.getVote().equalsIgnoreCase("upVote"))
        {
            boolean add = vRepo.findVote(vote);
            if (add)
            {
                return null;
            }
                else
            {
                vRepo.save(vote);
            }
        }
        else if (voteDTO.getVote().equalsIgnoreCase("downVote"))
        {
            boolean add = vRepo.findVote(vote);
            if (add)
            {
                return null;
            }
            else
            {
                vRepo.save(vote);
            }
        }
        else
        {
            return null;
        }

        return vote.toDto();
    }

}
