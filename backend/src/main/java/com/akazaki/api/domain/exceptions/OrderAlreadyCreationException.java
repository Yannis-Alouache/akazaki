package com.akazaki.api.domain.exceptions;

public class OrderAlreadyCreationException extends RuntimeException {
    public OrderAlreadyCreationException(String message) {
        super(message);
    }
}
