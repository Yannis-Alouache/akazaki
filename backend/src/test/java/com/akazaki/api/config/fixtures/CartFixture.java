package com.akazaki.api.config.fixtures;

import java.util.List;
import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.akazaki.api.domain.model.Cart;

@Component
public class CartFixture {
    public static final Cart adminUserCartWithDrinkItem = new Cart(1L, UserFixture.adminUser, new ArrayList<>(List.of(CartItemsFixture.drink)));
}
