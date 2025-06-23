package com.akazaki.api.infrastructure.stripe;

import java.util.HashMap;
import java.util.Map;

import com.akazaki.api.domain.ports.out.WebhookGateway;
import com.akazaki.api.infrastructure.exceptions.WebhookObjectExtractionFailedException;
import com.akazaki.api.infrastructure.exceptions.WebhookParsingFailedException;
import com.akazaki.api.infrastructure.exceptions.WebhookSignatureVerificationFailedException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;

public class FakeWebhookGateway implements WebhookGateway {

    private boolean shouldFailOnParseEvent = false;
    private boolean shouldFailOnVerifySignature = false;
    private boolean shouldFailOnExtractStripeObject = false;

    @Override
    public Event parseEvent(String payload) {
        if (shouldFailOnParseEvent)
            throw new WebhookParsingFailedException();
 
        Event event = new Event();
        return event;
    }
    
    @Override
    public Event verifySignature(String payload, String sigHeader) {
        if (shouldFailOnVerifySignature)
            throw new WebhookSignatureVerificationFailedException();

        Event event = new Event();
        event.setId("event_id");
        event.setType("payment_intent.succeeded");
        event.setData(null);

        return event;
    }

    @Override
    public StripeObject extractStripeObject(Event event) {
        if (shouldFailOnExtractStripeObject)
            throw new WebhookObjectExtractionFailedException();

        Map<String, String> metadata = new HashMap<>();
        metadata.put("orderId", "123");

        PaymentIntent paymentIntent = new PaymentIntent();
        paymentIntent.setId("payment_intent_id");
        paymentIntent.setPaymentMethod("pm_1234567890");
        paymentIntent.setMetadata(metadata);

        return paymentIntent;
    }

    public void simulateParseEventFailure() {
        shouldFailOnParseEvent = true;
    }

    public void simulateVerifySignatureFailure() {
        shouldFailOnVerifySignature = true;
    }

    public void simulateExtractStripeObjectFailure() {
        shouldFailOnExtractStripeObject = true;
    }
}
