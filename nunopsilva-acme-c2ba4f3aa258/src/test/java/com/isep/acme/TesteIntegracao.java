package com.isep.acme;

import com.isep.acme.model.Product;
import com.isep.acme.model.Rating;
import com.isep.acme.model.Review;
import com.isep.acme.model.User;
import com.isep.acme.model.dto.CreateReviewDTO;
import com.isep.acme.model.dto.ProductDTO;
import com.isep.acme.model.dto.ReviewDTO;
import com.isep.acme.repositories.h2.ProductRepository;
import com.isep.acme.repositories.h2.RatingRepository;
import com.isep.acme.repositories.h2.UserRepository;
import com.isep.acme.services.ProductService;
import com.isep.acme.services.RatingService;
import com.isep.acme.services.h2.ProductServiceImpl;
import com.isep.acme.services.h2.RatingServiceImpl;
import com.isep.acme.services.h2.ReviewServiceImpl;
import com.isep.acme.services.h2.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@SpringBootTest
public class TesteIntegracao {

    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    private ReviewServiceImpl reviewService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RatingServiceImpl ratingService;

    @Autowired
    private RatingRepository ratingRepository;


    @Test
    void testCreate() {


        Product product = new Product("123", "Product Name", "Product Description");


        ProductDTO result = productService.create(product);

        Optional<ProductDTO> productFound=  productService.findBySku(product.getSku());

        assertTrue(productFound.isPresent());
        assertEquals(product.getDesignation(),productFound.get().getDesignation());
        assertEquals(product.sku,productFound.get().getSku());
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

        Review review = new Review("Review text", date, product, "Fun Fact", rating, user);

        CreateReviewDTO createReviewDTO = new CreateReviewDTO("ReviewText",userOutput.getUserId(), 4.5);

        userRepository.findById(userOutput.getUserId());
        ratingRepository.findByRate(4.5);

        ReviewDTO reviewDTO= reviewService.create(createReviewDTO,product.sku);
        assertEquals(reviewDTO.getReviewText(),"ReviewText");

    }

}
