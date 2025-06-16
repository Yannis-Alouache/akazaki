package com.akazaki.api.domain.exceptions;

public class ProductNotInCartException extends RuntimeException {
    public ProductNotInCartException() {
        super("Product not in cart");
    }
}
