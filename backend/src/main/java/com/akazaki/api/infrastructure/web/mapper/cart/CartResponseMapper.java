package com.akazaki.api.infrastructure.web.mapper.cart;

import com.akazaki.api.domain.model.Cart;
import com.akazaki.api.infrastructure.web.dto.response.CartItemResponse;
import com.akazaki.api.infrastructure.web.dto.response.CartResponse;
import com.akazaki.api.infrastructure.web.mapper.cartItem.CartItemResponseMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CartResponseMapper {
    @Autowired
    private CartItemResponseMapper cartItemMapper;

    public CartResponse toResponse(Cart cart) {
        List<CartItemResponse> itemResponses = cartItemMapper.toResponseList(cart.getCartItems());
        return new CartResponse(cart.getId(), cart.getUser().getId(), itemResponses);
    }
}