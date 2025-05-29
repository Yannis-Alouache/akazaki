package com.akazaki.api.domain.exceptions;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Long userId) {
        super("Order not found for user with id: " + userId);
    }
}
