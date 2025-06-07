package com.akazaki.api.domain.exceptions;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException() {
        super("Cart not found");
    }
}
