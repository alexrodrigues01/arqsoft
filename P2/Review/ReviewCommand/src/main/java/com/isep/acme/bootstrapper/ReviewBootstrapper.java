package com.isep.acme.bootstrapper;

import com.isep.acme.model.Product;
import com.isep.acme.model.Review;
import com.isep.acme.model.user.User;
import com.isep.acme.model.vote.Vote;
import com.isep.acme.repositories.ProductRepository;
import com.isep.acme.repositories.ReviewRepository;
import com.isep.acme.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Component
@Order()
public class ReviewBootstrapper implements CommandLineRunner, Ordered {

    private ReviewRepository repository;
    private ProductRepository pRepository;
    private UserRepository uRepository;

    @Autowired
    public void setProductRepo(@Value("${review.repo}") String bean, @Value("${product.repo}") String productBean, @Value("${user.repo}") String userBean,
                               ApplicationContext applicationContext){
        repository = (ReviewRepository) applicationContext.getBean(bean);
        pRepository = (ProductRepository) applicationContext.getBean(productBean);
        uRepository = (UserRepository) applicationContext.getBean(userBean);
    }

    @Override
    public void run(String... args) throws Exception {

        if(repository.findById(1L).isEmpty()) {
            User user1 = uRepository.findById(3L).get();
            Product product = pRepository.findBySku("asd578fgh267").get();
            Review review = new Review(1L, 1, "pending", "review", Date.valueOf(LocalDate.now()), "funFact");
            List<Vote> vote = new ArrayList<>();
            vote.add(new Vote("upVote", 3L));
//            vote.add(new Vote("upVote", 4L));
//            vote.add(new Vote("upVote", 5L));
//            vote.add(new Vote("upVote", 6L));
//            vote.add(new Vote("upVote", 7L));
            review.setUpVote(vote);
            review.setDownVote(new ArrayList<>());
            review.setProduct(product);
            review.setUser(user1);

            repository.save(review);
        }

    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}
