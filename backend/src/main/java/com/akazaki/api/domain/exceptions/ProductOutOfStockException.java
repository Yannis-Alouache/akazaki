package com.akazaki.api.domain.exceptions;

public class ProductOutOfStockException extends RuntimeException {
    public ProductOutOfStockException() {
        super("This product is currently out of stock.");
    }
}