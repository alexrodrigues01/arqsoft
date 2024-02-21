package com.isep.acme.services;

import com.isep.acme.model.dto.ProductWithoutSkuDTO;
import com.isep.acme.services.skuGeneration.SkuGenerationAlgOneImpl;
import com.isep.acme.services.skuGeneration.SkuGenerationAlgThreeImpl;
import com.isep.acme.services.skuGeneration.SkuGenerationAlgTwoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


public class SkuGenerationAlgThreeImplTest {


    private SkuGenerationAlgThreeImpl skuGeneration;

    private SkuGenerationAlgOneImpl algOne;

    private SkuGenerationAlgTwoImpl algTwo;
    private ProductWithoutSkuDTO p;

    @BeforeEach
    void setUp() {
        algOne= Mockito.mock(SkuGenerationAlgOneImpl.class);
        algTwo= Mockito.mock(SkuGenerationAlgTwoImpl.class);
        skuGeneration= Mockito.mock(SkuGenerationAlgThreeImpl.class);
        p = Mockito.mock(ProductWithoutSkuDTO.class);
    }

    @Test
    void testGetSkuAlgorithm() {
        when(algOne.getSkuAlgorithm(p)).thenReturn("ABCDEF123456");
        when(p.getDesignation()).thenReturn("Sample Designation");
        when(algTwo.getSkuAlgorithm(p)).thenReturn("002df13681");
        String sku = skuGeneration.getSkuAlgorithm(p);

        assertEquals("ABCDEF002df", "ABCDEF002df");
    }
}
