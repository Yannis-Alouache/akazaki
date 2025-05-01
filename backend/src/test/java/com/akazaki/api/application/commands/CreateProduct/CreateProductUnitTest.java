package com.akazaki.api.application.commands.CreateProduct;

import static org.junit.Assert.*;

import java.util.Arrays;

import com.akazaki.api.infrastructure.persistence.Category.InMemoryCategoryRepository;
import com.akazaki.api.infrastructure.persistence.Product.InMemoryProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.akazaki.api.domain.exceptions.ProductAlreadyExistException;
import com.akazaki.api.domain.exceptions.UnableToFetchCategoriesException;
import com.akazaki.api.domain.model.Category;
import com.akazaki.api.domain.model.Product;
import com.akazaki.api.domain.ports.in.commands.CreateProductCommand;
import com.akazaki.api.domain.ports.out.CategoryRepository;
import com.akazaki.api.domain.ports.out.ProductRepository;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("Create Product Unit Tests")
class CreateProductUnitTest {
    private ProductRepository productRepository;

    private CreateProductCommandHandler handler;
    private Product expectedProduct;

    @BeforeEach
    public void setUp() {
        CategoryRepository categoryRepository = new InMemoryCategoryRepository();
        productRepository = new InMemoryProductRepository();

        handler = new CreateProductCommandHandler(productRepository, categoryRepository);

        // Setup test data
        Category category1 = new Category(1L, "Limonade");
        Category category2 = new Category(2L, "Raisin");

        categoryRepository.save(category1);
        categoryRepository.save(category2);
        
        expectedProduct = Product.create(
            "Test Product",
            "Test Description",
            10.0,
            100,
            Arrays.asList(category1, category2)
        );
        expectedProduct.setId(1L);
    }

    @Test
    @DisplayName("Create A Product Successfully")
    void createAProductSuccessfully() {
        // Arrange
        CreateProductCommand command = new CreateProductCommand(
            "Test Product",
            "Test Description",
            10.0,
            100,
            Arrays.asList(1L, 2L)
        );

        // Act
        handler.handle(command);
        Product product = productRepository.findById(1L).orElseThrow();

        // Assert
        assertNotNull(product);
        assertEquals(product.getId(), expectedProduct.getId());
        assertEquals(product.getName(), expectedProduct.getName());
        assertEquals(product.getDescription(), expectedProduct.getDescription());
        assertEquals(product.getPrice(), expectedProduct.getPrice(), 0.0);
        assertEquals(product.getStock(), expectedProduct.getStock());
        assertEquals(product.getCategories(), expectedProduct.getCategories());
    }

    @Test
    @DisplayName("Prevent Product Creation When Category Not Found")
    void preventProductCreationWhenCategoryNotFound() {
        // Arrange
        CreateProductCommand command = new CreateProductCommand(
            "Test Product",
            "Test Description",
            10.0,
            100,
            // Non existing categories
            Arrays.asList(3L, 4L)
        );

        // Act / Assert
        assertThrows(UnableToFetchCategoriesException.class, () -> handler.handle(command));
    }

    @Test
    @DisplayName("Prevent Product Creation When Already Existing")
    void preventProductCreationWhenAlreadyExisting() {
        // Arrange
        CreateProductCommand command = new CreateProductCommand(
            "Test Product",
            "Test Description",
            10.0,
            100,
            Arrays.asList(1L, 2L)
        );

        handler.handle(command);

        // Act / Assert
        assertThrows(ProductAlreadyExistException.class, () -> handler.handle(command));
    }
}