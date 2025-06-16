package com.akazaki.api.e2e;

import com.akazaki.api.domain.model.Category;
import com.akazaki.api.domain.model.Order;
import com.akazaki.api.domain.model.OrderItem;
import com.akazaki.api.domain.model.OrderStatus;
import com.akazaki.api.domain.model.Product;
import com.akazaki.api.domain.model.User;
import com.akazaki.api.domain.ports.out.CategoryRepository;
import com.akazaki.api.domain.ports.out.OrderRepository;
import com.akazaki.api.domain.ports.out.ProductRepository;
import com.akazaki.api.domain.ports.out.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@Transactional
@DisplayName("Order Controller Stock Availability Tests")
class StockAvailabilityE2ETest {

    private final MockMvc mockMvc;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    private User testUser;
    private Product testProduct;

    private static final int PRODUCT_STOCK = 30;

    @Autowired
    public StockAvailabilityE2ETest(MockMvc mockMvc, UserRepository userRepository, OrderRepository orderRepository, CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.mockMvc = mockMvc;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @BeforeEach
    void setUp() {
        User domainUser = User.create(null, "Doe", "John", "johndoe@akazaki.com", "encodedPassword", "0685357448", false);
        testUser = userRepository.save(domainUser);

        Category domainCategory = new Category(null, "Drink");
        Category testCategory = categoryRepository.save(domainCategory);

        Product domainProduct = Product.create(null, "Ramune Fraise", "the product description", 3.99, PRODUCT_STOCK, "/uploads/image.png", List.of(testCategory));
        testProduct = productRepository.save(domainProduct);
    }

    @Test
    @DisplayName("Should return stock available when all products have sufficient stock")
    @WithMockUser
    void should_return_stock_available_when_all_products_have_sufficient_stock() throws Exception {
        // Given
        Order domainOrderWithEnoughStock = Order.builder()
            .user(testUser)
            .date(LocalDateTime.now())
            .status(OrderStatus.PENDING)
            .items(List.of(OrderItem.create(PRODUCT_STOCK, 100.0, testProduct)))
            .totalPrice(100.0)
            .build();
        Order testOrderWithEnoughStock = orderRepository.save(domainOrderWithEnoughStock);

        Long orderId = testOrderWithEnoughStock.getId();

        // When & Then
        mockMvc.perform(get("/api/orders/{orderId}/stock-availability", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(orderId))
                .andExpect(jsonPath("$.isStockAvailable").value(true))
                .andExpect(jsonPath("$.missingItems").isEmpty());
    }

    @Test
    @DisplayName("Should return stock unavailable when some products have insufficient stock")
    @WithMockUser
    void should_return_stock_unavailable_when_some_products_have_insufficient_stock() throws Exception {
        // Given
        Order domainOrderWithInsufficientStock = Order.builder()
            .user(testUser)
            .date(LocalDateTime.now())
            .status(OrderStatus.PENDING)
            .items(List.of(OrderItem.create(PRODUCT_STOCK + 1, 100.0, testProduct)))
            .totalPrice(100.0)
            .build();
        Order testOrderWithInsufficientStock = orderRepository.save(domainOrderWithInsufficientStock);

        Long orderId = testOrderWithInsufficientStock.getId();

        // When & Then
        mockMvc.perform(get("/api/orders/{orderId}/stock-availability", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(orderId))
                .andExpect(jsonPath("$.isStockAvailable").value(false))
                .andExpect(jsonPath("$.missingItems").isNotEmpty());
    }
} 