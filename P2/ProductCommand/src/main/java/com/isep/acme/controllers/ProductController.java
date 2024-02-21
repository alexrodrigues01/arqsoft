package com.isep.acme.controllers;

import com.isep.acme.model.dto.ProductWithoutSkuDTO;
import com.isep.acme.rabbitmq.EventDTO;
import com.isep.acme.rabbitmq.Runner;
import com.isep.acme.rabbitmq.TypeOfEvent;
import com.isep.acme.services.ProductServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.isep.acme.model.Product;
import com.isep.acme.model.dto.ProductDTO;

import com.isep.acme.services.ProductService;

import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;


@Tag(name = "Product", description = "Endpoints for managing  products")
@RestController
@RequestMapping("/products")
class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private Runner runner;

    @Autowired
    public ProductServiceImpl service;


    @Operation(summary = "creates a product")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProductDTO> create(@RequestBody Product manager) {
        try {
            final ProductDTO product = service.create(manager);
            if (product.isPublished()) {
                runner.sendMessage(new EventDTO(TypeOfEvent.CREATE, "Product", product));
            }
            return new ResponseEntity<ProductDTO>(product, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Product must have a unique SKU.");
        }
    }

    @Operation(summary = "creates a product with sku generated")
    @PostMapping("/createWithoutSku/")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProductDTO> createWithoutSku(@RequestBody ProductWithoutSkuDTO productWithNoSku) {
        try {
            final ProductDTO product = service.createWithoutSku(productWithNoSku);
            if (product.isPublished()) {
                runner.sendMessage(new EventDTO(TypeOfEvent.CREATE, "Product", product));
            }
            return new ResponseEntity<ProductDTO>(product, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Product must have a unique SKU.");
        }
    }

    @Operation(summary = "updates a product")
    @PatchMapping(value = "/{sku}")
    public ResponseEntity<ProductDTO> Update(@PathVariable("sku") final String sku, @RequestBody final Product product) {

        final ProductDTO productDTO = service.updateBySku(sku, product);

        if (productDTO == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found.");
        else {
            try {
                if (productDTO.isPublished()) {
                    runner.sendMessage(new EventDTO(TypeOfEvent.UPDATE, "Product", productDTO));
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return ResponseEntity.ok().body(productDTO);
        }
    }


    @Operation(summary = "approves a product")
    @PatchMapping(value = "/approve/{sku}/{userName}")
    public ResponseEntity<ProductDTO> Update(@PathVariable("sku") final String sku, @PathVariable("userName") final String userName) {
        final ProductDTO productDTO;
        try {
            productDTO = service.approveProduct(sku, userName);
        } catch (ResourceNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found.");
        }

        if (productDTO == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found.");
        else {
            try {
                if (productDTO.isPublished()) {
                    runner.sendMessage(new EventDTO(TypeOfEvent.CREATE, "Product", productDTO));
                    runner.sendMessage(new EventDTO(TypeOfEvent.UPDATE, "Product", productDTO));
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return ResponseEntity.ok().body(productDTO);
        }
    }

    @Operation(summary = "deletes a product")
    @DeleteMapping(value = "/{sku}")
    public ResponseEntity<Product> delete(@PathVariable("sku") final String sku) {
        Optional<ProductDTO> productDTO = service.findBySku(sku);
        service.deleteBySku(sku);
        try {
            if (productDTO.isPresent() && productDTO.get().isPublished()) {
                runner.sendMessage(new EventDTO(TypeOfEvent.DELETE, "Product", productDTO));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.noContent().build();
    }
}