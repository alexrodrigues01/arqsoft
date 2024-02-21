package com.isep.acme.services;

import com.isep.acme.repositories.ProductRepository;
import com.isep.acme.repositories.ReviewRepository;
import com.isep.acme.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.isep.acme.model.Rating;
import com.isep.acme.repositories.RatingRepository;

import java.util.Optional;

@Service
public class RatingServiceImpl implements RatingService{

    private RatingRepository repository;

    @Autowired
    public void setProductRepo(@Value("${rating.repo}") String bean, ApplicationContext applicationContext){
        repository = (RatingRepository) applicationContext.getBean(bean);
    }

    public Optional<Rating> findByRate(Double rate){
        return repository.findByRate(rate);
    }

}
