package com.isep.acme.services.acme;

import com.isep.acme.model.Product;
import com.isep.acme.model.Rating;
import com.isep.acme.model.Review;
import com.isep.acme.model.User;
import com.isep.acme.model.dto.CreateReviewDTO;
import com.isep.acme.model.dto.ProductDTO;
import com.isep.acme.model.dto.ReviewDTO;
import com.isep.acme.repositories.ProductRepository;
import com.isep.acme.repositories.RatingRepository;
import com.isep.acme.repositories.ReviewRepository;
import com.isep.acme.repositories.UserRepository;
import com.isep.acme.services.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
public class TesteIntegracao {

    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    private ReviewServiceImpl reviewService;

    @Autowired
    private UserServiceImpl userService;

    private UserRepository userRepository;

    @Autowired
    private RatingServiceImpl ratingService;

    private RatingRepository ratingRepository;

    @Autowired
    public void setRepos(@Value("${rating.repo}") String bean, @Value("${user.repo}") String userBean,
                         ApplicationContext applicationContext){
        ratingRepository = (RatingRepository) applicationContext.getBean(bean);
        userRepository = (UserRepository) applicationContext.getBean(userBean);
    }

    @Test
    void testCreate() {


        Product product = new Product("123", "Product Name", "Product Description");


        ProductDTO result = productService.create(product);

        Optional<ProductDTO> productFound=  productService.findBySku(product.getSku());

        assertTrue(productFound.isPresent());
        assertEquals(product.getDesignation(),productFound.get().getDesignation());
        assertEquals(product.getSku(),productFound.get().getSku());
        productService.deleteBySku("123");
    }

    @Test
    void testCreateReview(){

        String sku = "124";

        LocalDate date = LocalDate.of(2023, 1, 1);
        Rating rating = new Rating(4.5);
        Product product = new Product(sku, "Product Name", "Product Description");
        User user = new User("username@email.com", "password","CARLOS","247312415","Rua de Cima");
        user.setUserId(1L);
        User userOutput= userRepository.save(user);
        productService.create(product);

        Review review = new Review("Review text", Date.valueOf(date), product, "Fun Fact", rating, user);

        CreateReviewDTO createReviewDTO = new CreateReviewDTO("ReviewText",userOutput.getUserId(), 4.5);

        userRepository.findById(userOutput.getUserId());
        ratingRepository.findByRate(4.5);

        ReviewDTO reviewDTO= reviewService.create(createReviewDTO,product.getSku());
        assertEquals(reviewDTO.getReviewText(),"ReviewText");
        productService.deleteBySku(sku);
        userRepository.delete(user);
    }

}
