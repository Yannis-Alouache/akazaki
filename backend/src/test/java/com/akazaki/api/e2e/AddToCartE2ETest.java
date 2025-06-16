package com.akazaki.api.e2e;

import com.akazaki.api.config.JwtFactory;
import com.akazaki.api.domain.model.Category;
import com.akazaki.api.domain.model.Product;
import com.akazaki.api.domain.model.User;
import com.akazaki.api.domain.ports.out.CategoryRepository;
import com.akazaki.api.domain.ports.out.ProductRepository;
import com.akazaki.api.domain.ports.out.UserRepository;
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
@DisplayName("Add To cart E2E Tests")
public class AddToCartE2ETest {
    private final MockMvc mockMvc;
    private final JwtFactory jwtFactory;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    private String jwtToken;
    private Product product;

    @Autowired
    public AddToCartE2ETest(MockMvc mockMvc, JwtFactory jwtFactory, UserRepositoryAdapter userRepository, ProductRepositoryAdapter productRepository, CategoryRepositoryAdapter categoryRepository) {
        this.mockMvc = mockMvc;
        this.jwtFactory = jwtFactory;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @BeforeEach
    void setUp() {
        User domainUser = User.create(null, "Doe", "John", "johndoe@akazaki.com", "encodedPassword", "0685357448", false);
        User persistedUser = userRepository.save(domainUser);

        Category domainCategory = new Category(null, "Drink");
        Category persistedCategory = categoryRepository.save(domainCategory);

        Product domainProduct = Product.create(null, "Ramune Fraise", "the product description", 3.99, 30, "/uploads/image.png", List.of(persistedCategory));
        this.product = productRepository.save(domainProduct);

        this.jwtToken = jwtFactory.getJwtToken(persistedUser);
    }

    @Test
    @DisplayName("Add Item To Cart Successfully")
    public void addToCartSuccessfully() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/cart/add/" + product.getId())
                        .header("Authorization", "Bearer " + jwtToken))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cartItems[0].product.id").value(product.getId()));
    }

    @Test
    @DisplayName("Allow item to be added to cart multiple times increasing the quantity")
    public void allowItemToBeAddedMultipleTimes() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/cart/add/" + product.getId())
                .header("Authorization", "Bearer " + jwtToken));
    
        mockMvc.perform(MockMvcRequestBuilders.post("/api/cart/add/" + product.getId())
                .header("Authorization", "Bearer " + jwtToken))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.cartItems[0].quantity").value(2));
    }

    @Test
    @DisplayName("return 404 when adding a product that does not exist")
    public void addToCartWithNonExistentProduct() throws Exception {
        long nonExistentProductId = 999999L;

        mockMvc.perform(MockMvcRequestBuilders.post("/api/cart/add/" + nonExistentProductId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
