package com.akazaki.api.application.commands.AddToCart;

import java.util.ArrayList;

import com.akazaki.api.domain.exceptions.UserNotFoundException;
import com.akazaki.api.domain.model.User;
import com.akazaki.api.domain.ports.out.UserRepository;
import org.springframework.stereotype.Component;

import com.akazaki.api.domain.exceptions.ProductNotFoundException;
import com.akazaki.api.domain.exceptions.ProductOutOfStockException;
import com.akazaki.api.domain.model.Cart;
import com.akazaki.api.domain.model.CartItem;
import com.akazaki.api.domain.model.Product;
import com.akazaki.api.domain.ports.in.commands.AddToCartCommand;
import com.akazaki.api.domain.ports.out.CartRepository;
import com.akazaki.api.domain.ports.out.ProductRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AddToCartCommandHandler {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    public Cart handle(AddToCartCommand command) {
        int quantity = 1;

        User user = userRepository.findById(command.userId()).orElseThrow(
            () -> new UserNotFoundException(command.userId())
        );

        Cart cart = cartRepository.findByUserId(command.userId()).orElse(
            Cart.builder()
                .user(user)
                .cartItems(new ArrayList<>())
                .build()
        );

        Product product = productRepository.findById(command.productId()).orElseThrow(
            ProductNotFoundException::new
        );

        if (product.getStock() < quantity) throw new ProductOutOfStockException();

        CartItem cartItem = CartItem.builder()
            .product(product)
            .quantity(quantity)
            .build();
        
        cart.addItem(cartItem);

        return cartRepository.save(cart);
    }
}
