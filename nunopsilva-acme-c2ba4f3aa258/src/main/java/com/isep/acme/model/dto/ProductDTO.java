package com.isep.acme.model.dto;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class ProductDTO {
    private String sku;
    private String designation;

    public ProductDTO(String sku, String designation) {
        this.sku = sku;
        this.designation = designation;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }
}