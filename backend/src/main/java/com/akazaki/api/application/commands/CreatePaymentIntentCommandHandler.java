package com.akazaki.api.application.commands;

import org.springframework.stereotype.Component;

import com.akazaki.api.domain.exceptions.OrderNotFoundException;
import com.akazaki.api.domain.exceptions.UserNotFoundException;
import com.akazaki.api.domain.model.Order;
import com.akazaki.api.domain.ports.in.commands.CreatePaymentIntentCommand;
import com.akazaki.api.domain.ports.out.OrderRepository;
import com.akazaki.api.domain.ports.out.PaymentGateway;
import com.akazaki.api.domain.ports.out.UserRepository;

@Component
public class CreatePaymentIntentCommandHandler {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final PaymentGateway paymentGateway;

    public CreatePaymentIntentCommandHandler(UserRepository userRepository, OrderRepository orderRepository, PaymentGateway paymentGateway) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.paymentGateway = paymentGateway;
    }

    public String handle(CreatePaymentIntentCommand command) {
        // 1. Vérifier user
        userRepository.findById(command.userId())
            .orElseThrow(() -> new UserNotFoundException(command.userId()));

        // 2. Récupérer l'order
        Order order = orderRepository.findById(command.orderId())
            .orElseThrow(() -> new OrderNotFoundException());

        // 3. Créer le Payment Intent
        return paymentGateway.createPaymentIntent(order);
    }
}
