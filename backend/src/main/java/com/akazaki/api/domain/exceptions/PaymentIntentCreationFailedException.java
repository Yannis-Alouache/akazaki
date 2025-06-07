package com.akazaki.api.domain.exceptions;

public class PaymentIntentCreationFailedException extends RuntimeException {
    public PaymentIntentCreationFailedException() {
        super("Payment Intent creation failed");
    }
}
