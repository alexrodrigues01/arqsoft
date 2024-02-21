package com.isep.acme.model;

import com.isep.acme.model.dto.ProductDTO;
import lombok.Getter;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Property;

import java.util.Objects;

@Node("Product")
@Getter
public class ProductNeo4J {
    @Id
    @GeneratedValue
    private Long id; // Neo4j uses 'id' as the default primary key field

    @Property
    private String sku;

    @Property
    private String designation;

    @Property
    private String description;

    public ProductNeo4J() {
        // Default constructor
    }

    public ProductNeo4J(String sku, String designation, String description) {
        this.sku = sku;
        this.designation = designation;
        this.description = description;
    }

    public ProductNeo4J(final Long productID, final String sku, final String designation, final String description) {
        this(productID, sku);
        setDescription(description);
        setDesignation(designation);
    }
    public ProductNeo4J(final Long productID, final String sku) {
        this.id = Objects.requireNonNull(productID);
        setSku(sku);
    }
    public void updateProduct(ProductNeo4J p) {
        setDesignation(p.designation);
        setDescription(p.description);
    }

    public void setDesignation(String designation) {
        if (designation == null || designation.isBlank()) {
            throw new IllegalArgumentException("Designation is a mandatory attribute of Product.");
        }
        if (designation.length() > 50) {
            throw new IllegalArgumentException("Designation must not be greater than 50 characters.");
        }
        this.designation = designation;
    }
    public void setDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description is a mandatory attribute of Product.");
        }

        if (description.length() > 1200) {
            throw new IllegalArgumentException("Description must not be greater than 1200 characters.");
        }

        this.description = description;
    }

    public void setSku(String sku) {
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("SKU is a mandatory attribute of Product.");
        }
//        if (sku.length() < 10 || sku.length() > 12) {
//           throw new IllegalArgumentException("SKU must be 12 characters long.");
//        }

        this.sku = sku;
    }

        public ProductDTO toDto() {
        return new ProductDTO(this.sku, this.designation);
    }

}
