package com.akazaki.api.application.commands.CreateCheckoutSession;

import org.springframework.stereotype.Component;

import com.akazaki.api.domain.model.Order;
import com.akazaki.api.domain.ports.in.commands.CreateCheckoutSessionCommand;
import com.akazaki.api.domain.ports.out.OrderRepository;
import com.akazaki.api.domain.ports.out.PaymentGateway;
import com.akazaki.api.domain.ports.out.UserRepository;
import com.akazaki.api.domain.exceptions.OrderNotFoundException;
import com.akazaki.api.domain.exceptions.UserNotFoundException;


@Component
public class CreateCheckoutSessionCommandHandler {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final PaymentGateway paymentGateway;

    public CreateCheckoutSessionCommandHandler(UserRepository userRepository, OrderRepository orderRepository, PaymentGateway paymentGateway) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.paymentGateway = paymentGateway;
    }

    public String handle(CreateCheckoutSessionCommand command) {
        // step 1: get the user and verify if it exists
        userRepository.findById(command.userId())
            .orElseThrow(() -> new UserNotFoundException(command.userId()));

        // step 2: get the order
        Order order = orderRepository.findById(command.orderId())
            .orElseThrow(() -> new OrderNotFoundException());

        // step 3: create the checkout session
        String clientSecret = paymentGateway.createCheckoutSession(order);

        return clientSecret;
    }
}
