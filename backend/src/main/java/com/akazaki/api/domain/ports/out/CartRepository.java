package com.akazaki.api.domain.ports.out;

import java.util.Optional;

import com.akazaki.api.domain.model.Cart;

public interface CartRepository {
    Optional<Cart> findByUserId(Long userId);
    Cart save(Cart cart);
}
