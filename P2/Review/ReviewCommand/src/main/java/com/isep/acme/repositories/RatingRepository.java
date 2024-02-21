package com.isep.acme.repositories;

import org.springframework.data.repository.CrudRepository;
import com.isep.acme.model.Rating;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface RatingRepository extends CrudRepository<Rating, Long> {

    //@Query("SELECT r FROM Rating r WHERE r.rate=:rate")
    Optional<Rating> findByRate(Double rate);

}
