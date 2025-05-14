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
@DisplayName("List Products E2E Tests")
public class ListProductsE2ETest {
    private final MockMvc mockMvc;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ListProductsE2ETest(MockMvc mockMvc, JwtFactory jwtFactory, ProductRepositoryAdapter productRepository, CategoryRepositoryAdapter categoryRepository) {
        this.mockMvc = mockMvc;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @BeforeEach
    void setUp() {
        Category domainCategory = new Category(null, "Drink");
        Category category = categoryRepository.save(domainCategory);

        Product domainProduct = Product.create(null, "Ramune Fraise", "the product description", 3.99, 30, "/uploads/image.png", List.of(category));
        Product domainProduct2 = Product.create(null, "Ultra Ice Tea Dragon Ball Super Végéta 🥬", "the product description", 1.99, 10, "/uploads/image.png", List.of(category));
        productRepository.save(domainProduct);
        productRepository.save(domainProduct2);
    }

    @Test
    @DisplayName("List Products Successfully")
    void listProductsSuccessfully() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/products"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2));
    }
} 