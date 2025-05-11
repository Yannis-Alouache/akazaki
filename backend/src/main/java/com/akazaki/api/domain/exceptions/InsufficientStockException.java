package com.akazaki.api.domain.exceptions;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException() {
        super("The requested quantity exceeds the available stock");
    }
}