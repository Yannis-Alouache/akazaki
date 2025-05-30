package com.akazaki.api.infrastructure.web.controller.webhook;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.model.v2.Event;
import com.stripe.net.Webhook;

@RestController
@RequestMapping("/api/")
public class WebhookController {

    // TODO: Implement the webhook
    @PostMapping("/stripe/webhook")
    public ResponseEntity<String> handleStripeWebhook() { 
        throw new UnsupportedOperationException("Unimplemented method 'handleStripeWebhook'");
    }
}