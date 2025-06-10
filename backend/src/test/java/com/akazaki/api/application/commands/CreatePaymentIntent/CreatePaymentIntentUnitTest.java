package com.akazaki.api.application.commands.CreatePaymentIntent;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.akazaki.api.application.commands.CreatePaymentIntentCommandHandler;
import com.akazaki.api.config.fixtures.OrderFixture;
import com.akazaki.api.config.fixtures.UserFixture;
import com.akazaki.api.domain.exceptions.OrderNotFoundException;
import com.akazaki.api.domain.exceptions.UserNotFoundException;
import com.akazaki.api.domain.model.Order;
import com.akazaki.api.domain.model.User;
import com.akazaki.api.domain.ports.in.commands.CreatePaymentIntentCommand;
import com.akazaki.api.domain.ports.out.OrderRepository;
import com.akazaki.api.domain.ports.out.UserRepository;
import com.akazaki.api.infrastructure.persistence.Order.InMemoryOrderRepository;
import com.akazaki.api.infrastructure.persistence.User.InMemoryUserRepository;
import com.akazaki.api.infrastructure.stripe.FakePaymentIntentGayeway;

@DisplayName("Create Payment Intent Unit Tests")
public class CreatePaymentIntentUnitTest {

    private CreatePaymentIntentCommandHandler handler;
    private User user;
    private Order order;

    @BeforeEach
    void setUp() {
        FakePaymentIntentGayeway paymentGateway = new FakePaymentIntentGayeway();
        UserRepository userRepository = new InMemoryUserRepository();
        OrderRepository orderRepository = new InMemoryOrderRepository();

        user = userRepository.save(UserFixture.basicUser);
        order = orderRepository.save(OrderFixture.orderWithItems);

        handler = new CreatePaymentIntentCommandHandler(userRepository, orderRepository, paymentGateway);
    }

    @Test
    @DisplayName("Create Payment Intent Successfully")
    void createPaymentIntentSuccessfully() {
        // Given
        CreatePaymentIntentCommand command = new CreatePaymentIntentCommand(user.getId(), order.getId());

        // When
        String paymentIntentId = handler.handle(command);

        // Then
        assertThat(paymentIntentId).isNotNull();
    }

    @Test
    @DisplayName("Create Payment Intent With Invalid Order")
    void createPaymentIntentWithInvalidOrder() {
        // Given
        CreatePaymentIntentCommand command = new CreatePaymentIntentCommand(user.getId(), 999L);

        // When/Then
        assertThrows(OrderNotFoundException.class, () -> handler.handle(command));
    }

    @Test
    @DisplayName("Create Payment Intent With Invalid User")
    void createPaymentIntentWithInvalidUser() {
        // Given
        CreatePaymentIntentCommand command = new CreatePaymentIntentCommand(999L, order.getId());

        // When/Then
        assertThrows(UserNotFoundException.class, () -> handler.handle(command));
    }

}