package com.akazaki.api.application.queries;

import com.akazaki.api.domain.exceptions.UserNotFoundException;
import com.akazaki.api.domain.model.Cart;
import com.akazaki.api.domain.model.User;
import com.akazaki.api.domain.ports.in.queries.GetCartQuery;
import com.akazaki.api.domain.ports.out.CartRepository;
import com.akazaki.api.domain.ports.out.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GetCartQueryHandler {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    public Cart handle(GetCartQuery query) {
        User user = userRepository.findById(query.userId())
            .orElseThrow(() -> new UserNotFoundException(query.userId()));
        
        Optional<Cart> cart = cartRepository.findByUserId(query.userId());
        
        if (cart.isPresent())
            return cart.get();
        else {
            Cart newCart = Cart.builder()
                .user(user)
                .cartItems(new ArrayList<>())
                .build();
    
            return cartRepository.save(newCart);
        }
    }
} 