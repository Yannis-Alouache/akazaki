package com.akazaki.api.config.fixtures;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import com.akazaki.api.domain.model.CartItem;

import jakarta.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class CartItemsFixture {
    public final ProductFixture productFixture;


    public CartItem drink;

    @PostConstruct
    public void init() {
        drink =  CartItem.builder()
                    .quantity(1)
                    .product(productFixture.drink)
                    .build();
    }
}
