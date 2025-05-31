package com.akazaki.api.infrastructure.web.controller.webhook;

import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.model.Event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.akazaki.api.infrastructure.stripe.StripeWebhookService;

@RestController
@RequestMapping("/api/")
public class WebhookController {

    private static final Logger log = LoggerFactory.getLogger(WebhookController.class);

    @Value("whsec_b2148f99137f118d81a59b2cce99e2561c1003f5b9a9e6f4572706d9d92e5498")
    private String endpointSecret;

    private final StripeWebhookService stripeWebhookService;

    public WebhookController(StripeWebhookService stripeWebhookService) {
        this.stripeWebhookService = stripeWebhookService;
    }

    // TODO: Implement the webhook
    @PostMapping("/stripe/webhook")
    public ResponseEntity<String> handleStripeWebhook(
        @RequestBody String payload,
        @RequestHeader("Stripe-Signature") String sigHeader
    ) {
            Event event = stripeWebhookService.parseEvent(payload);

            if(endpointSecret != null && sigHeader != null) {
                event = stripeWebhookService.verifySignature(payload, sigHeader);
            }

            StripeObject stripeObject = stripeWebhookService.extractStripeObject(event);

            // Handle the event
            switch (event.getType()) {
                case "payment_intent.succeeded":
                    PaymentIntent paymentIntent = (PaymentIntent) stripeObject;
                    System.out.println("Payment: " + paymentIntent );
                    // Then define and call a method to handle the successful payment intent.
                    // handlePaymentIntentSucceeded(paymentIntent);
                    break;
                default:
                    System.out.println("Unhandled event type: " + event.getType());
                break;
            }
            return ResponseEntity.ok("OK");
    }
}