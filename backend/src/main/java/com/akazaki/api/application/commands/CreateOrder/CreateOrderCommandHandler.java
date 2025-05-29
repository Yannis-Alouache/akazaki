package com.akazaki.api.application.commands.CreateOrder;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.akazaki.api.domain.exceptions.UnableToCreateOrderException;
import com.akazaki.api.domain.model.Cart;
import com.akazaki.api.domain.model.Order;
import com.akazaki.api.domain.model.OrderItem;
import com.akazaki.api.domain.model.OrderStatus;
import com.akazaki.api.domain.ports.in.commands.CreateOrderCommand;
import com.akazaki.api.domain.ports.out.CartRepository;
import com.akazaki.api.domain.ports.out.OrderRepository;

@Component
public class CreateOrderCommandHandler {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;

    public CreateOrderCommandHandler(
        CartRepository cartRepository,
        OrderRepository orderRepository
    ) {
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
    }

    public Order handle(CreateOrderCommand command) {
        Cart cart = cartRepository.findByUserId(command.userId()).orElseThrow(
            () -> new UnableToCreateOrderException()
        );
        validateCart(cart);

        Order order = Order.draft(
            LocalDateTime.now(),
            OrderStatus.PENDING,
            OrderItem.fromCartItems(cart.getCartItems()),
            cart.getTotalPrice()
        );

        return orderRepository.save(order);
    }

    private void validateCart(Cart cart) {
        if (cart.getCartItems().isEmpty()) {
            throw new UnableToCreateOrderException();
        }
    }
}
