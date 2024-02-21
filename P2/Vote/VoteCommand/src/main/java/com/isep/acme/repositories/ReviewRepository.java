package com.isep.acme.repositories;

import com.isep.acme.model.Review;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface ReviewRepository extends CrudRepository<Review,Long> {

    Optional<Review> findById(Long reviewId);
}
