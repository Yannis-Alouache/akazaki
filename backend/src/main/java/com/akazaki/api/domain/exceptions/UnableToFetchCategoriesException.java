package com.akazaki.api.domain.exceptions;

public class UnableToFetchCategoriesException extends RuntimeException {
    public UnableToFetchCategoriesException() {
        super("Unable to fetch categories");
    }
}