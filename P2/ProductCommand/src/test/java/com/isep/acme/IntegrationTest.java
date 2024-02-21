//package com.isep.acme;
//
//
//import com.isep.acme.model.Product;
//import com.isep.acme.repositories.mongo.MongoProductRepositoryImp;
//import com.isep.acme.repositories.mongo.MongoUserRepositoryImp;
//import com.isep.acme.services.ProductServiceImpl;
//import com.isep.acme.services.skuGeneration.SkuGenerationAlgTwoImpl;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//
//@SpringBootTest
//public class IntegrationTest {
//
//    private ProductServiceImpl productService;
//
//    @Autowired
//    private MongoProductRepositoryImp productRepositoryImp;
//
//    @Autowired
//    private MongoUserRepositoryImp userRepositoryImp;
//
//    @Autowired
//    private SkuGenerationAlgTwoImpl skuGenerationAlgTwo;
//
//
//
//    @BeforeEach
//    void setUp(){
//        productService= new ProductServiceImpl();
//        productService.setProductRepository(productRepositoryImp);
//        productService.setUserRepository(userRepositoryImp);
//        productService.setSkuGeneration(skuGenerationAlgTwo);
//
//    }
//
//    @Test
//    public void testApproveProduct() {
//        String userName= "admin1@mail.com";
//        String userName2="admin2@mail.com";
//        String sku= "sku12345";
//        productService.create(new Product(sku,"description1","designation2"));
//        productService.approveProduct(sku,userName);
//        Product productResult= productService.getProductBySku(sku).get();
//        Assertions.assertEquals(productResult.getProductManagerApprovals().get(0).getUsername(), userName);
//        productService.approveProduct(sku,userName2);
//        Product productResult2= productService.getProductBySku(sku).get();
//        Assertions.assertEquals(productResult2.getProductManagerApprovals().get(1).getUsername(), userName2);
//        Assertions.assertTrue(productResult2.isPublished());
//        productService.deleteBySku("sku12345");
//    }
//}
