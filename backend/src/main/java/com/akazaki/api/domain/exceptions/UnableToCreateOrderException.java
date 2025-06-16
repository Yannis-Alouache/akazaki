package com.akazaki.api.domain.exceptions;

public class UnableToCreateOrderException extends RuntimeException {
    public UnableToCreateOrderException() {
        super("Unable to create order");
    }
}
