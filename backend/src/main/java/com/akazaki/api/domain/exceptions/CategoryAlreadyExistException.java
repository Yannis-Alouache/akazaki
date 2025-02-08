package com.akazaki.api.domain.exceptions;

public class CategoryAlreadyExistException extends RuntimeException {
    public CategoryAlreadyExistException() {
        super("Category with this name already exists");
    }
}