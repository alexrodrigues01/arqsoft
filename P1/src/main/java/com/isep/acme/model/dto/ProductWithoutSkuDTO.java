package com.isep.acme.model.dto;

public class ProductWithoutSkuDTO {

    private String designation;

    private String description;

    public ProductWithoutSkuDTO(String designation, String description) {
        this.designation = designation;
        this.description = description;
    }

    public String getDesignation() {
        return designation;
    }

    public String getDescription() {
        return description;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
