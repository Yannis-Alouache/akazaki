package com.akazaki.api.infrastructure.persistence.Cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akazaki.api.domain.model.Cart;
import com.akazaki.api.infrastructure.persistence.CartItem.CartItemMapper;
import com.akazaki.api.infrastructure.persistence.User.UserMapper;

@Component
public class CartMapper {
    @Autowired
    UserMapper userMapper;

    @Autowired
    CartItemMapper cartItemMapper;

    public CartEntity toEntity(Cart domain) {
        return CartEntity.builder()
          .id(domain.getId())
          .user(userMapper.toEntity(domain.getUser()))
          .cartItems(cartItemMapper.toEntityList(domain.getCartItems()))
          .build();
    }

    public Cart toDomain(CartEntity entity) {
        return Cart.builder()
         .id(entity.getId())
         .user(userMapper.toDomain(entity.getUser()))
         .cartItems(cartItemMapper.toDomainList(entity.getCartItems()))
         .build();
    }
}
