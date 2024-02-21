package com.isep.acme.services;

import com.isep.acme.model.dto.ProductWithoutSkuDTO;

public interface SkuGeneration {

    String getSkuAlgorithm(ProductWithoutSkuDTO p);
}
