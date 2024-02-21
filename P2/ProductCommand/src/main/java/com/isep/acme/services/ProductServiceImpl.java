package com.isep.acme.services;


import com.isep.acme.controllers.ResourceNotFoundException;
import com.isep.acme.model.Product;
import com.isep.acme.model.User;
import com.isep.acme.model.dto.ProductDTO;
import com.isep.acme.model.dto.ProductDetailDTO;
import com.isep.acme.model.dto.ProductWithoutSkuDTO;
import com.isep.acme.repositories.ProductRepository;
import com.isep.acme.repositories.UserRepository;
import com.isep.acme.repositories.mongo.MongoProductRepositoryImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    public ProductRepository repository;

    public UserRepository userRepository;
    public SkuGeneration skuGenerator;

    @Autowired
    public void setUserRepository(@Value("${user.repo}") String bean, ApplicationContext applicationContext) {
        userRepository = (UserRepository) applicationContext.getBean(bean);
    }

    @Autowired
    public void setProductRepo(@Value("${product.repo}") String bean, ApplicationContext applicationContext) {
        repository = (ProductRepository) applicationContext.getBean(bean);
    }

    @Autowired
    public void setSkuGeneration(@Value("${sku.generation}") String bean, ApplicationContext applicationContext) {
        skuGenerator = (SkuGeneration) applicationContext.getBean(bean);
    }


    public void setProductRepository(MongoProductRepositoryImp repository) {
        this.repository = repository;
    }


    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public void setSkuGeneration(SkuGeneration skuGenerator) {
        this.skuGenerator = skuGenerator;
    }

    @Override
    public Optional<Product> getProductBySku(final String sku) {

        return repository.findBySku(sku);
    }

    @Override
    public Optional<ProductDTO> findBySku(String sku) {
        final Optional<Product> product = repository.findBySku(sku);

        if (product.isEmpty())
            return Optional.empty();
        else
            return Optional.of(product.get().toDto());
    }


    @Override
    public Iterable<ProductDTO> findByDesignation(final String designation) {
        Iterable<Product> p = repository.findByDesignation(designation);
        List<ProductDTO> pDto = new ArrayList<>();
        for (Product pd : p) {
            pDto.add(pd.toDto());
        }

        return pDto;
    }

    @Override
    public Iterable<ProductDTO> getCatalog() {
        Iterable<Product> p = repository.findAll();
        List<ProductDTO> pDto = new ArrayList<>();
        for (Product pd : p) {
            pDto.add(pd.toDto());
        }

        return pDto;
    }

    public ProductDetailDTO getDetails(String sku) {

        Optional<Product> p = repository.findBySku(sku);

        if (p.isEmpty())
            return null;
        else
            return new ProductDetailDTO(p.get().getSku(), p.get().getDesignation(), p.get().getDescription());
    }


    @Override
    public ProductDTO create(final Product product) {
        final Product p = new Product(product.getSku(), product.getDesignation(), product.getDescription());

        return repository.save(p).toDto();
    }

    @Override
    public ProductDTO createWithoutSku(final ProductWithoutSkuDTO productWithoutSku) {
        final Product p = new Product(skuGenerator.getSkuAlgorithm(productWithoutSku), productWithoutSku.getDesignation(), productWithoutSku.getDescription());

        return repository.save(p).toDto();
    }

    @Override
    public ProductDTO updateBySku(String sku, Product product) {

        final Optional<Product> productToUpdate = repository.findBySku(sku);

        if (productToUpdate.isEmpty()) return null;

        productToUpdate.get().updateProduct(product);

        Product productUpdated = repository.save(productToUpdate.get());

        return productUpdated.toDto();
    }

    @Override
    public void deleteBySku(String sku) {
        final Optional<Product> productToDelete = repository.findBySku(sku);

        if (productToDelete.isEmpty()) return;

        repository.delete(productToDelete.get());
    }

    @Override
    public ProductDTO approveProduct(String sku, String userName) {
        final Optional<Product> productToApprove = repository.findBySku(sku);
        final Optional<User> productManager = userRepository.findByUsername(userName);
        if (productToApprove.isPresent()) {
            if (productManager.isPresent()) {
                productToApprove.get().approveProduct(productManager.get());
                return repository.save(productToApprove.get()).toDto();
            }
        }
        throw new ResourceNotFoundException("Product or User doesn't exist");
    }
}
