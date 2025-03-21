package com.akazaki.api.application.commands.CreateOrder;

import com.akazaki.api.domain.model.Order;
import com.akazaki.api.domain.ports.out.OrderRepository;
import com.akazaki.api.domain.ports.in.commands.CreateOrderCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateOrderCommandHandler {
    private final OrderRepository orderRepository;

    public Order handle(CreateOrderCommand command) {
        Order order = new Order(command.date(), command.status(), command.totalPrice(), command.billingAddress(), command.shippingAddress());
        return orderRepository.save(order);
    }
}