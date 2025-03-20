package com.akazaki.api.infrastructure.exceptions;

public class InvalidFileTypeException extends RuntimeException {
    public InvalidFileTypeException() {
        super("Unable to save file. Only images are allowed.");
    }
}
