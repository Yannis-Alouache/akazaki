package com.akazaki.api.infrastructure.exceptions;

public class PaymentIntentCreationFailedException extends RuntimeException {
    public PaymentIntentCreationFailedException() {
        super("Payment Intent creation failed");
    }
}
