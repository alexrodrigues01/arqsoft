package com.isep.acme.services.skuGeneration;

import com.isep.acme.model.dto.ProductWithoutSkuDTO;
import com.isep.acme.services.SkuGeneration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SkuGenerationAlgThreeImpl")
public class SkuGenerationAlgThreeImpl implements SkuGeneration {

    @Autowired
    SkuGenerationAlgOneImpl algOne;

    @Autowired
    SkuGenerationAlgTwoImpl algTwo;
    
    @Override
    public String getSkuAlgorithm(ProductWithoutSkuDTO p) {

        String algorithmPartOne = algOne.getSkuAlgorithm(p).substring(0, 6);
        String algorithmPartTwo = algTwo.getSkuAlgorithm(p).substring(0, 5);

        return algorithmPartOne + algorithmPartTwo;
    }
}
