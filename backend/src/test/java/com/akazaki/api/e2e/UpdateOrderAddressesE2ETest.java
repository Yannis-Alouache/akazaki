package com.akazaki.api.e2e;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.akazaki.api.infrastructure.persistence.Order.OrderRepositoryAdapter;
import com.akazaki.api.infrastructure.persistence.User.UserRepositoryAdapter;
import com.akazaki.api.infrastructure.web.dto.request.AddressRequest;
import com.akazaki.api.infrastructure.web.dto.request.UpdateOrderAddressesRequest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@Transactional
@DisplayName("Update Order Addresses E2E Tests")
class UpdateOrderAddressesE2ETest {

    private final MockMvc mockMvc;
    private final JwtFactory jwtFactory;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;

    private String jwtToken;
    private String otherUserJwtToken;
    private Order testOrder;
    private User testUser;
    private User otherUser;

    @Autowired
    public UpdateOrderAddressesE2ETest(
            MockMvc mockMvc, 
            JwtFactory jwtFactory, 
            UserRepositoryAdapter userRepository,
            OrderRepositoryAdapter orderRepository,
            ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.jwtFactory = jwtFactory;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.objectMapper = objectMapper;
    }

    @BeforeEach
    void setUp() {
        // Créer les utilisateurs de test
        User domainUser = User.create(null, "Doe", "John", "johndoe@akazaki.com", "encodedPassword", "0685357448", false);
        testUser = userRepository.save(domainUser);

        User domainOtherUser = User.create(null, "Smith", "Jane", "janesmith@akazaki.com", "encodedPassword", "0987654321", false);
        otherUser = userRepository.save(domainOtherUser);

        // Créer une commande de test
        Order domainOrder = Order.builder()
            .user(testUser)
            .date(LocalDateTime.now())
            .status(OrderStatus.PENDING)
            .items(List.of())
            .totalPrice(100.0)
            .build();
        testOrder = orderRepository.save(domainOrder);

        // Générer les tokens JWT
        jwtToken = jwtFactory.getJwtToken(testUser);
        otherUserJwtToken = jwtFactory.getJwtToken(otherUser);
    }

    @Test
    @DisplayName("Should update order addresses successfully")
    void shouldUpdateOrderAddressesSuccessfully() throws Exception {
        // Given
        AddressRequest billingAddress = new AddressRequest(
            "Dupont", "Pierre", "123", "Rue de la Paix", 
            "Apt 4", "75001", "Paris", "France"
        );
        
        AddressRequest shippingAddress = new AddressRequest(
            "Martin", "Marie", "456", "Avenue des Champs", 
            null, "75008", "Paris", "France"
        );
        
        UpdateOrderAddressesRequest request = new UpdateOrderAddressesRequest(
            billingAddress, shippingAddress
        );

        String requestBody = objectMapper.writeValueAsString(request);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/orders/" + testOrder.getId() + "/address")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(testOrder.getId()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.billingAddress.lastName").value("Dupont"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.billingAddress.firstName").value("Pierre"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.billingAddress.street").value("Rue de la Paix"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.shippingAddress.lastName").value("Martin"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.shippingAddress.firstName").value("Marie"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.shippingAddress.street").value("Avenue des Champs"));
    }

    @Test
    @DisplayName("Should return not found when order does not exist")
    void shouldReturnNotFoundWhenOrderDoesNotExist() throws Exception {
        // Given
        AddressRequest billingAddress = new AddressRequest(
            "Dupont", "Pierre", "123", "Rue de la Paix", 
            "Apt 4", "75001", "Paris", "France"
        );
        
        AddressRequest shippingAddress = new AddressRequest(
            "Martin", "Marie", "456", "Avenue des Champs", 
            null, "75008", "Paris", "France"
        );
        
        UpdateOrderAddressesRequest request = new UpdateOrderAddressesRequest(
            billingAddress, shippingAddress
        );

        String requestBody = objectMapper.writeValueAsString(request);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/orders/999/address")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Should return forbidden when user is not order owner")
    void shouldReturnForbiddenWhenUserIsNotOrderOwner() throws Exception {
        // Given
        AddressRequest billingAddress = new AddressRequest(
            "Dupont", "Pierre", "123", "Rue de la Paix", 
            "Apt 4", "75001", "Paris", "France"
        );
        
        AddressRequest shippingAddress = new AddressRequest(
            "Martin", "Marie", "456", "Avenue des Champs", 
            null, "75008", "Paris", "France"
        );
        
        UpdateOrderAddressesRequest request = new UpdateOrderAddressesRequest(
            billingAddress, shippingAddress
        );

        String requestBody = objectMapper.writeValueAsString(request);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/orders/" + testOrder.getId() + "/address")
                .header("Authorization", "Bearer " + otherUserJwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isForbidden())
            .andExpect(MockMvcResultMatchers.jsonPath("$.reason").value("Vous n'êtes pas autorisé à modifier cette commande"));
    }
} 