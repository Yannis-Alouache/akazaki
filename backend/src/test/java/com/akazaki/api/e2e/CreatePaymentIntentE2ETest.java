package com.akazaki.api.e2e;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.akazaki.api.config.JwtFactory;
import com.akazaki.api.domain.model.Order;
import com.akazaki.api.domain.model.OrderStatus;
import com.akazaki.api.domain.model.User;
import com.akazaki.api.domain.ports.out.OrderRepository;
import com.akazaki.api.domain.ports.out.UserRepository;
import com.akazaki.api.infrastructure.web.dto.request.CreatePaymentIntentRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@Transactional
@DisplayName("Create Payment Intent E2E Tests")
public class CreatePaymentIntentE2ETest {

    private final MockMvc mockMvc;
    private final JwtFactory jwtFactory;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;

    private User testUser;
    private Order testOrder;
    private String jwtToken;

    @Autowired
    public CreatePaymentIntentE2ETest(MockMvc mockMvc, JwtFactory jwtFactory, UserRepository userRepository, OrderRepository orderRepository, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.jwtFactory = jwtFactory;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.objectMapper = objectMapper;
    }

    @BeforeEach
    void setUp() {
        User domainUser = User.create(null, "Doe", "John", "johndoe@akazaki.com", "encodedPassword", "0685357448", false);
        testUser = userRepository.save(domainUser);

        Order domainOrder = Order.builder()
            .user(testUser)
            .date(LocalDateTime.now())
            .status(OrderStatus.PENDING)
            .items(List.of())
            .totalPrice(100.0)
            .build();
        testOrder = orderRepository.save(domainOrder);

        this.jwtToken = jwtFactory.getJwtToken(testUser);
    }

    @Test
    @DisplayName("Create Payment Intent Successfully")
    void createPaymentIntentSuccessfully() throws Exception {
        // Given
        Long orderId = testOrder.getId();

        CreatePaymentIntentRequest request = new CreatePaymentIntentRequest(orderId);
        String requestBody = objectMapper.writeValueAsString(request);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/payments/create-payment-intent")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.clientSecret").isNotEmpty());
    }
}
