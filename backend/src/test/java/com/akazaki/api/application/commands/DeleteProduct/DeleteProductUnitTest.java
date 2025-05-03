package com.akazaki.api.application.commands.DeleteProduct;

import com.akazaki.api.domain.model.Category;
import com.akazaki.api.domain.model.Product;
import com.akazaki.api.domain.ports.in.commands.DeleteProductCommand;
import com.akazaki.api.domain.ports.out.ProductRepository;
import com.akazaki.api.infrastructure.persistence.Product.InMemoryProductRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.util.Arrays;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Delete Product Unit Test")
public class DeleteProductUnitTest {
    private static ProductRepository productRepository;
    private static DeleteProductCommandHandler commandHandler;
    private static Product expectedProduct;

    @BeforeAll
    public static void setUp() {
        productRepository = new InMemoryProductRepository();

        commandHandler = new DeleteProductCommandHandler(productRepository);

        // Setup test data
        Category category1 = new Category(1L, "Limonade");
        Category category2 = new Category(2L, "Raisin");

        expectedProduct = Product.create(
                "Test Product",
                "Test Description",
                10.0,
                100,
                Arrays.asList(category1, category2)
        );
        expectedProduct.setId(1L);
        productRepository.save(expectedProduct);
    }

    @Test
    @DisplayName("Delete A Product Successfully")
    public void deleteAProductSuccessfully() {
        commandHandler.handle(new DeleteProductCommand(expectedProduct.getId()));
        assertThat(productRepository.findById(expectedProduct.getId())).isNotPresent();
    }
}
