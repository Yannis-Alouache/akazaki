package com.akazaki.api.e2e;

import com.akazaki.api.config.JwtFactory;
import com.akazaki.api.domain.model.Category;
import com.akazaki.api.domain.model.Product;
import com.akazaki.api.domain.model.User;
import com.akazaki.api.infrastructure.persistence.Category.CategoryRepositoryAdapter;
import com.akazaki.api.infrastructure.persistence.Product.ProductRepositoryAdapter;
import com.akazaki.api.infrastructure.persistence.User.UserRepositoryAdapter;
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
@DisplayName("Delete Product E2E Tests")
public class DeleteProductE2ETest {
    private final MockMvc mockMvc;
    private final UserRepositoryAdapter userRepository;
    private final ProductRepositoryAdapter productRepository;
    private final CategoryRepositoryAdapter categoryRepository;
    private final JwtFactory jwtFactory;

    private String jwtToken;
    private Category category;

    @Autowired
    public DeleteProductE2ETest(MockMvc mockMvc, JwtFactory jwtFactory, UserRepositoryAdapter userRepository, ProductRepositoryAdapter productRepository, CategoryRepositoryAdapter categoryRepository) {
        this.mockMvc = mockMvc;
        this.jwtFactory = jwtFactory;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @BeforeEach
    void setUp() {
        User domainUser = User.create(null, "Doe", "John", "johndoe@akazaki.com", "encodedPassword", "0685357448", true);
        User user = userRepository.save(domainUser);

        Category domainCategory = new Category(null, "Drink");
        this.category = categoryRepository.save(domainCategory);

        this.jwtToken = jwtFactory.getJwtToken(user);
    }

    @Test
    @DisplayName("Delete A Product Successfully")
    public void deleteProductSuccessfully() throws Exception {
        Product domainProduct = Product.create(null, "Ramune Fraise", "the product description", 3.99, 30, "/uploads/image.png", List.of(category));
        Product product = productRepository.save(domainProduct);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/admin/products/" + product.getId())
                        .header("Authorization", "Bearer " + jwtToken))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
