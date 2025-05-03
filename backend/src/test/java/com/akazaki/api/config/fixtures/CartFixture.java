package com.akazaki.api.config.fixtures;

import java.util.List;

import org.springframework.stereotype.Component;

import com.akazaki.api.domain.model.Cart;
import com.akazaki.api.domain.ports.out.CartRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CartFixture {
    public final CartRepository cartRepository;
    public final UserFixture userFixture;
    public final CartItemsFixture cartItemsFixture;


    public Cart basicUserCart;

    @PostConstruct
    public void init() {
        basicUserCart = new Cart(1L, userFixture.basicUser, List.of(cartItemsFixture.drink));
    }
}
