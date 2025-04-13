package com.akazaki.api.infrastructure.persistence.Cart;

import com.akazaki.api.domain.model.Cart;
import com.akazaki.api.domain.ports.out.CartRepository;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("prod")
@RequiredArgsConstructor
public class CartRepositoryAdapter implements CartRepository {
    private final JpaCartRepository repository;
    private final CartMapper mapper;
    
    @Override
    public Optional<Cart> findByUserId(Long userId) {
        Optional<CartEntity> cartEntity = repository.findByUserId(userId);
        return cartEntity.map(mapper::toDomain);
    }

    @Override
    public Cart save(Cart cart) {
        CartEntity cartEntity = mapper.toEntity(cart);
        CartEntity savedCartEntity = repository.save(cartEntity);
        return mapper.toDomain(savedCartEntity);
    }

    
}