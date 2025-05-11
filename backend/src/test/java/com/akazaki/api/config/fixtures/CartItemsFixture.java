package com.akazaki.api.config.fixtures;

import com.akazaki.api.domain.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import com.akazaki.api.domain.model.CartItem;

import jakarta.annotation.PostConstruct;

@Component
public class CartItemsFixture {
    public final static CartItem drink = CartItem.builder()
                                            .quantity(1)
                                            .product(ProductFixture.drink)
                                            .build();
}
