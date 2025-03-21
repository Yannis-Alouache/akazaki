package com.akazaki.api.infrastructure.exceptions;

public class UnableToSaveFileException extends RuntimeException {
    public UnableToSaveFileException() {
        super("Unable to save file. Try again later.");
    }
}
