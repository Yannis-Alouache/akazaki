package com.akazaki.api.application.commands.CreateOrder;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.akazaki.api.domain.exceptions.UnableToCreateOrderException;
import com.akazaki.api.domain.model.Cart;
import com.akazaki.api.domain.model.Order;
import com.akazaki.api.domain.model.OrderItem;
import com.akazaki.api.domain.model.OrderStatus;
import com.akazaki.api.domain.model.User;
import com.akazaki.api.domain.ports.in.commands.CreateOrderCommand;
import com.akazaki.api.domain.ports.out.CartRepository;
import com.akazaki.api.domain.ports.out.OrderRepository;
import com.akazaki.api.domain.ports.out.UserRepository;

@Component
public class CreateOrderCommandHandler {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public CreateOrderCommandHandler(
        CartRepository cartRepository,
        OrderRepository orderRepository,
        UserRepository userRepository
    ) {
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    public Order handle(CreateOrderCommand command) {
        User user = userRepository.findById(command.userId()).orElseThrow(
            () -> new UnableToCreateOrderException()
        );

        Cart cart = cartRepository.findByUserId(command.userId()).orElseThrow(
            () -> new UnableToCreateOrderException()
        );
        validateCart(cart);

        Order order = Order.draft(
            user,
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
