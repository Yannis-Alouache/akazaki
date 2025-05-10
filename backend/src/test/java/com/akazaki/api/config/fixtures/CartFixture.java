package com.akazaki.api.config.fixtures;

import java.util.List;

import org.springframework.stereotype.Component;

import com.akazaki.api.domain.model.Cart;
import com.akazaki.api.domain.ports.out.CartRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
public class CartFixture {
    public static final Cart basicUserCart = new Cart(1L, UserFixture.basicUser, List.of(CartItemsFixture.drink));
}
