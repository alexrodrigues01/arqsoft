package com.isep.acme.services.acme.services;

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

@SpringBootTest
public class SkuGenerationAlgThreeImplTest {

    @InjectMocks
    private SkuGenerationAlgThreeImpl skuGeneration;
    @Mock
    private SkuGenerationAlgOneImpl algOne;
    @Mock
    private SkuGenerationAlgTwoImpl algTwo;
    private ProductWithoutSkuDTO p;

    @BeforeEach
    void setUp() {
        p = Mockito.mock(ProductWithoutSkuDTO.class);
    }

    @Test
    void testGetSkuAlgorithm() {
        when(algOne.getSkuAlgorithm(p)).thenReturn("ABCDEF123456");
        when(p.getDesignation()).thenReturn("Sample Designation");
        when(algTwo.getSkuAlgorithm(p)).thenReturn("002df13681");
        String sku = skuGeneration.getSkuAlgorithm(p);

        assertEquals("ABCDEF002df", sku);
    }
}
