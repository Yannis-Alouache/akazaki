package com.akazaki.api.config.fixtures;

import org.springframework.stereotype.Component;

import com.akazaki.api.domain.model.CartItem;


@Component
public class CartItemsFixture {
    public final static CartItem drink = CartItem.builder()
                                            .quantity(1)
                                            .product(ProductFixture.ramuneFraise)
                                            .build();
}
