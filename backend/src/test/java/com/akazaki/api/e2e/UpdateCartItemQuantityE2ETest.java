package com.akazaki.api.e2e;


import com.akazaki.api.infrastructure.persistence.Cart.CartRepositoryAdapter;
import com.akazaki.api.infrastructure.persistence.Category.CategoryRepositoryAdapter;

import java.util.List;

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

import com.akazaki.api.config.JwtFactory;
import com.akazaki.api.domain.model.Cart;
import com.akazaki.api.domain.model.CartItem;
import com.akazaki.api.domain.model.Category;
import com.akazaki.api.domain.model.Product;
import com.akazaki.api.domain.model.User;
import com.akazaki.api.domain.ports.out.CartRepository;
import com.akazaki.api.domain.ports.out.CategoryRepository;
import com.akazaki.api.domain.ports.out.ProductRepository;
import com.akazaki.api.domain.ports.out.UserRepository;
import com.akazaki.api.infrastructure.persistence.Product.ProductRepositoryAdapter;
import com.akazaki.api.infrastructure.persistence.User.UserRepositoryAdapter;



@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@Transactional
@DisplayName("Update cart item quantity E2E test")
class UpdateCartItemQuantityE2ETest {

    private final MockMvc mockMvc;
    private final JwtFactory jwtFactory;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CartRepository cartRepository;

    private String jwtToken;
    private Product product;
    private final int PRODUCT_STOCK = 30;

    @Autowired
    public UpdateCartItemQuantityE2ETest(MockMvc mockMvc, JwtFactory jwtFactory, UserRepositoryAdapter userRepository, ProductRepositoryAdapter productRepository, CategoryRepositoryAdapter categoryRepository, CartRepositoryAdapter cartRepository) {
        this.mockMvc = mockMvc;
        this.jwtFactory = jwtFactory;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.cartRepository = cartRepository;
    }

    @BeforeEach
    void setUp() {
        Category domainCategory = new Category(null, "Drink");
        Category category = categoryRepository.save(domainCategory);

        Product domainProduct = Product.create(null, "Ramune Fraise", "the product description", 3.99, PRODUCT_STOCK, "/uploads/image.png", List.of(category));
        this.product = productRepository.save(domainProduct);

        User domainUser = User.create(null, "Doe", "John", "johndoe@akazaki.com", "encodedPassword", "0685357448", false);
        User user = userRepository.save(domainUser);

        Cart cart = Cart.builder().user(user).cartItems(List.of(CartItem.builder().product(product).quantity(1).build())).build();
        cartRepository.save(cart);

        this.jwtToken = jwtFactory.getJwtToken(user);
    }

    @Test
    @DisplayName("should update cart item quantity")
    void shouldUpdateCartItemQuantity() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/cart/items/" + product.getId() + "?quantity=2")
                .header("Authorization", "Bearer " + jwtToken))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.cartItems[0].quantity").value(2));
    }

    @Test
    @DisplayName("should return bad request when quantity exceeds stock")
    void shouldReturnBadRequestWhenQuantityExceedsStock() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/cart/items/" + product.getId() + "?quantity=" + (PRODUCT_STOCK + 1))
                        .header("Authorization", "Bearer " + jwtToken))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("should return not found when product does not exist")
    void shouldReturnNotFoundWhenProductDoesNotExist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/cart/items/" + 9999 + "?quantity=1")
                        .header("Authorization", "Bearer " + jwtToken))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("should remove item from cart when quantity is 0")
    void shouldRemoveItemFromCartWhenQuantityIs0() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/cart/items/" + product.getId() + "?quantity=0")
                        .header("Authorization", "Bearer " + jwtToken))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cartItems").isEmpty());
    }
} 