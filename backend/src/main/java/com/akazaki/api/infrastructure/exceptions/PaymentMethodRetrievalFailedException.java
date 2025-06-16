package com.akazaki.api.infrastructure.exceptions;

public class PaymentMethodRetrievalFailedException extends RuntimeException {
    public PaymentMethodRetrievalFailedException() {
        super("Payment Method retrieval failed");
    }
}
