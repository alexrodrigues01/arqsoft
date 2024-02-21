package com.isep.acme.controllers;
import com.isep.acme.model.ProdImage;
import com.isep.acme.model.Product;
import com.isep.acme.property.UploadFileResponse;
import com.isep.acme.repositories.ImageRepository;
import com.isep.acme.repositories.h2.ProductRepository;
import com.isep.acme.services.FileStorageService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
class FileControllerTest {

    @InjectMocks
    FileController fileController;

    @Mock
    FileStorageService fileStorageService;

    @Mock
    ImageRepository imageRepository;

    @Mock
    ProductRepository productRepository;

    @Test
    void findById() {
        Long imageId = 1L;
        ProdImage expectedProdImage = new ProdImage();

        when(imageRepository.findById(eq(imageId))).thenReturn(Optional.of(expectedProdImage));

        ResponseEntity<ProdImage> actualResponse = fileController.findById(imageId);

        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(expectedProdImage, actualResponse.getBody());
    }

    @Test
    void uploadFile() {
        MultipartFile mockFile = new MockMultipartFile("file", "test.txt", "text/plain", "Hello, World!".getBytes());
        String expectedFileName = "test.txt";
        String expectedFileDownloadUri = "http://localhost/downloadFile/test.txt";

        when(fileStorageService.storeFile(eq(mockFile))).thenReturn(expectedFileName);

        UploadFileResponse actualResponse = fileController.uploadFile(mockFile);

        assertEquals(expectedFileName, actualResponse.getFileName());
        assertEquals(expectedFileDownloadUri, actualResponse.getFileDownloadUri());
        assertEquals(mockFile.getContentType(), actualResponse.getFileType());
        assertEquals(mockFile.getSize(), actualResponse.getSize());
    }

    @Test
    void uploadMultipleFiles() {
        // Create mock data
        MultipartFile mockFile1 = new MockMultipartFile("file", "file1.txt", "text/plain", "File 1".getBytes());
        MultipartFile mockFile2 = new MockMultipartFile("file", "file2.txt", "text/plain", "File 2".getBytes());

        // Mock the fileStorageService to return valid responses
        when(fileStorageService.storeFile(eq(mockFile1)))
                .thenReturn("file1.txt");
        when(fileStorageService.storeFile(eq(mockFile2)))
                .thenReturn("file2.txt");

        // Call the method
        List<UploadFileResponse> actualResponses = fileController.uploadMultipleFiles(new MultipartFile[]{mockFile1, mockFile2});

        // Assertions
        assertEquals(2, actualResponses.size());
        assertEquals("file1.txt", actualResponses.get(0).getFileName());
        assertEquals("http://localhost/downloadFile/file2.txt", actualResponses.get(0).getFileDownloadUri());
        assertEquals("text/plain", actualResponses.get(0).getFileType());
        assertEquals(6L, actualResponses.get(0).getSize());

        assertEquals("file2.txt", actualResponses.get(1).getFileName());
        assertEquals("http://localhost/downloadFile/file2.txt", actualResponses.get(1).getFileDownloadUri());
        assertEquals("text/plain", actualResponses.get(1).getFileType());
        assertEquals(6L, actualResponses.get(1).getSize());
    }



    @Test
    void findByID() {
        // Create mock data
        Long productId = 1L;
        Product expectedProduct = new Product(productId,"SKU","designação","descrição"); // Populate with expected data

        // Mock ProductRepository behavior
        when(productRepository.findById(eq(productId))).thenReturn(Optional.of(expectedProduct));

        // Call the method
        ResponseEntity<Product> actualResponse = fileController.findByID(productId);

        // Assertions
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(expectedProduct, actualResponse.getBody());
    }
}

