package com.akazaki.api.infrastructure.web.mapper.cartItem;

import com.akazaki.api.domain.model.CartItem;
import com.akazaki.api.infrastructure.web.dto.response.CartItemResponse;
import com.akazaki.api.infrastructure.web.dto.response.ProductResponse;
import com.akazaki.api.infrastructure.web.mapper.product.ProductResponseMapper;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartItemResponseMapper {

    private final ProductResponseMapper productMapper;

    CartItemResponseMapper(ProductResponseMapper productMapper) {
        this.productMapper = productMapper;
    }

    public CartItemResponse toResponse(CartItem cartItem) {
        ProductResponse productResponse = productMapper.toResponse(cartItem.getProduct());
        return new CartItemResponse(cartItem.getId(), cartItem.getQuantity(), productResponse);
    }

    public List<CartItemResponse> toResponseList(List<CartItem> cartItems) {
        return cartItems.stream()
                    .map(this::toResponse)
                    .collect(Collectors.toList());
    }
}