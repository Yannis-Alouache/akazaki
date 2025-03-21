package com.akazaki.api.domain.exceptions;

public class ProductAlreadyExistException extends RuntimeException {
    public ProductAlreadyExistException() {
        super("Product already exists");
    }
}