package com.akazaki.api.application.commands;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.akazaki.api.domain.exceptions.OrderNotFoundException;
import com.akazaki.api.domain.model.Order;
import com.akazaki.api.domain.model.Payment;
import com.akazaki.api.domain.model.PaymentMethodEnum;
import com.akazaki.api.domain.ports.in.commands.CreatePaymentCommand;
import com.akazaki.api.domain.ports.out.OrderRepository;
import com.akazaki.api.domain.ports.out.PaymentGateway;
import com.akazaki.api.domain.ports.out.PaymentRepository;


@Component
public class CreatePaymentCommandHandler {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentGateway paymentGateway;

    public CreatePaymentCommandHandler(PaymentRepository paymentRepository, OrderRepository orderRepository, PaymentGateway paymentGateway) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.paymentGateway = paymentGateway;
    }

    public Payment handle(CreatePaymentCommand command) {
        Order order = orderRepository.findById(command.orderId())
            .orElseThrow(() -> new OrderNotFoundException());

        PaymentMethodEnum paymentMethod = paymentGateway.getPaymentMethod(command.paymentMethodId());

        Payment payment = Payment.create(
            order,
            order.getTotalPrice(),
            paymentMethod,
            LocalDateTime.now()
        );

        return paymentRepository.save(payment);
    }
}
