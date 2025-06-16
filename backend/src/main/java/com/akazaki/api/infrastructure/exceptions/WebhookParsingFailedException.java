package com.akazaki.api.infrastructure.exceptions;

public class WebhookParsingFailedException extends RuntimeException {
    public WebhookParsingFailedException() {
        super("Webhook error while parsing basic request.");
    }
}
