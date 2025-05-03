package com.akazaki.api.infrastructure.persistence.Cart;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import com.akazaki.api.domain.model.Cart;
import com.akazaki.api.domain.model.CartItem;
import com.akazaki.api.domain.ports.out.CartRepository;

@Repository
@Profile("test")
public class InMemoryCartRepository implements CartRepository {
    private final List<Cart> carts = new ArrayList<>();
    
    @Override
    public Optional<Cart> findByUserId(Long userId) {
        return carts.stream()
            .filter(cart -> cart.getUser().getId().equals(userId))
            .findFirst();
    }

    @Override
    public Cart save(Cart cart) {
        cart.setId((long) (carts.size() + 1));

        List<CartItem> items = cart.getCartItems();
        for (int i = 0; i < items.size(); i++) {
            CartItem item = items.get(i);
            item.setId((long) (i + 1)); 
        }

        carts.add(cart);
        return cart;
    }
}