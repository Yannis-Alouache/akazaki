package com.akazaki.api.infrastructure.exceptions;

public class WebhookObjectExtractionFailedException extends RuntimeException {
    public WebhookObjectExtractionFailedException() {
        super("Webhook object extraction failed");
    }
}
