package com.akazaki.api.infrastructure.exceptions;

public class UnableToDeleteFileException extends RuntimeException {
    public UnableToDeleteFileException() {
        super("Unable to delete file. Try again later.");
    }
} 