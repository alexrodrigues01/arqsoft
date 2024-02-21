package com.isep.acme.repositories;

import com.isep.acme.model.Vote;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface VoteRepository extends CrudRepository<Vote,Long> {

    boolean findVote(Vote vote);

}
