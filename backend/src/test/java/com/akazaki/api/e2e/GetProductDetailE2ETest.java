package com.akazaki.api.e2e;

import com.akazaki.api.config.JwtFactory;
import com.akazaki.api.domain.model.Category;
import com.akazaki.api.domain.model.Product;
import com.akazaki.api.domain.ports.out.CategoryRepository;
import com.akazaki.api.domain.ports.out.ProductRepository;
import com.akazaki.api.infrastructure.persistence.Category.CategoryRepositoryAdapter;
import com.akazaki.api.infrastructure.persistence.Product.ProductRepositoryAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@Transactional
@DisplayName("Get Product Detail E2E Tests")
public class GetProductDetailE2ETest {
    private final MockMvc mockMvc;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    private String jwtToken;
    private Product product;

    @Autowired
    public GetProductDetailE2ETest(MockMvc mockMvc, JwtFactory jwtFactory, ProductRepositoryAdapter productRepository, CategoryRepositoryAdapter categoryRepository) {
        this.mockMvc = mockMvc;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @BeforeEach
    void setUp() {
        Category domainCategory = new Category(null, "Drink");
        Category category = categoryRepository.save(domainCategory);

        Product domainProduct = Product.create(null, "Ramune Fraise", "the product description", 3.99, 30, "/uploads/image.png", List.of(category));
        this.product = productRepository.save(domainProduct);
    }

    @Test
    @DisplayName("Get Product Detail Successfully")
    void getProductDetailSuccessfully() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/products/" + this.product.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("return 404 when product not found")
    void getProductDetailNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/products/999999"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}