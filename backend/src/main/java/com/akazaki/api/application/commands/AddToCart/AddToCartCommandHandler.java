package com.akazaki.api.application.commands.AddToCart;

import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    CartRepository cartRepository;

    @Autowired
    ProductRepository productRepository;

    public void handle(AddToCartCommand command) {
        int quantity = 1;

        Cart cart = cartRepository.findByUserId(command.userId()).orElse(
            Cart.builder().build()
        );

        Product product = productRepository.findById(command.productId()).orElseThrow(
            () -> new ProductNotFoundException()
        );

        if (product.getStock() < quantity) throw new ProductOutOfStockException();

        CartItem cartItem = CartItem.builder()
            .product(product)
            .quantity(quantity)
            .build();
        
        cart.addItem(cartItem);

        cartRepository.save(cart);
    }
}
