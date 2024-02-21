package com.isep.acme.bootstrapper;

import com.isep.acme.model.Review;
import com.isep.acme.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order()
public class ReviewBootstrapper implements CommandLineRunner{

    private ReviewRepository repository;

    @Autowired
    public void setProductRepo(@Value("${review.repo}") String bean,
                               ApplicationContext applicationContext){
        repository = (ReviewRepository) applicationContext.getBean(bean);
    }

    @Override
    public void run(String... args) throws Exception {

        if(repository.findById(1L).isEmpty()) {
            Review review = new Review(1L);
            repository.save(review);
        }

    }
}
