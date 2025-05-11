package com.akazaki.api.application.commands.StoreImage;

import com.akazaki.api.domain.exceptions.ProductNotFoundException;
import com.akazaki.api.domain.ports.in.commands.StoreImageCommand;
import com.akazaki.api.infrastructure.exceptions.InvalidFileTypeException;
import com.akazaki.api.infrastructure.persistence.Image.InMemoryImageRepository;
import com.akazaki.api.infrastructure.persistence.Product.InMemoryProductRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.FileSystemResource;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Store Image Unit Tests")
public class StoreImageUnitTest {

    private StoreImageCommandHandler handler;

    @BeforeEach
    void setUp() {
        InMemoryProductRepository productRepository = new InMemoryProductRepository();
        InMemoryImageRepository imageRepository = new InMemoryImageRepository();
        handler = new StoreImageCommandHandler(imageRepository, productRepository);
    }

    @Test
    @DisplayName("Prevent Storing Other Files Than Images")
    void preventStoringOtherFilesThanImages() throws IOException {
        // Given
        FileSystemResource file = new FileSystemResource("src/test/resources/images/test-image.png");
        StoreImageCommand command = new StoreImageCommand(
                1L,
                file.getFilename(),
                "application/pdf",
                file.getInputStream()
        );

        // When/Then - Second creation should fail
        assertThrows(InvalidFileTypeException.class, () -> handler.handle(command));
    }

    @Test
    @DisplayName("Prevent Storing If Product Not Found")
    void preventStoringIfProductNotFound() throws IOException {
        // Given
        FileSystemResource file = new FileSystemResource("src/test/resources/images/test-image.png");
        StoreImageCommand command = new StoreImageCommand(
                1L,
                file.getFilename(),
                "image/png",
                file.getInputStream()
        );

        // When/Then - Second creation should fail
        assertThrows(ProductNotFoundException.class, () -> handler.handle(command));
    }
}
