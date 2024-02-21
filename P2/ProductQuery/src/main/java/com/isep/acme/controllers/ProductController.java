package com.isep.acme.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
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
    private ProductServiceImpl service;

    @Operation(summary = "gets catalog, i.e. all products")
    @GetMapping
    public ResponseEntity<Iterable<ProductDTO>> getCatalog() throws JsonProcessingException {
        final var products = service.getCatalog();

        runner.sendMessage(new EventDTO(TypeOfEvent.GET, "Product", products));
        return ResponseEntity.ok().body(products);
    }

    @Operation(summary = "finds product by sku")
    @GetMapping(value = "/{sku}")
    public ResponseEntity<ProductDTO> getProductBySku(@PathVariable("sku") final String sku) throws JsonProcessingException {

        final Optional<ProductDTO> product = service.findBySku(sku);

        if (product.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found.");
        else {
            runner.sendMessage(new EventDTO(TypeOfEvent.GET, "Product", product));
            return ResponseEntity.ok().body(product.get());
        }
    }

    @Operation(summary = "finds product by designation")
    @GetMapping(value = "/designation/{designation}")
    public ResponseEntity<Iterable<ProductDTO>> findAllByDesignation(@PathVariable("designation") final String designation) throws JsonProcessingException {

        final Iterable<ProductDTO> products = service.findByDesignation(designation);
        runner.sendMessage(new EventDTO(TypeOfEvent.GET,"Product",products));
        return ResponseEntity.ok().body(products);
    }
}