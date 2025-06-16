package com.akazaki.api.infrastructure.exceptions;

public class WebhookSignatureVerificationFailedException extends RuntimeException {
    public WebhookSignatureVerificationFailedException() {
        super("Webhook signature verification failed");
    }
}
