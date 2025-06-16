package com.akazaki.api.infrastructure.stripe;

import org.springframework.stereotype.Service;

import com.akazaki.api.domain.ports.out.WebhookGateway;
import com.akazaki.api.infrastructure.exceptions.WebhookObjectExtractionFailedException;
import com.akazaki.api.infrastructure.exceptions.WebhookParsingFailedException;
import com.akazaki.api.infrastructure.exceptions.WebhookSignatureVerificationFailedException;
import com.stripe.model.Event;
import com.stripe.model.StripeObject;

@Service
public class FakeWebhookGateway implements WebhookGateway {
    private boolean shouldThrowParsingException = false;
    private boolean shouldThrowSignatureException = false;
    private boolean shouldThrowObjectExtractionException = false;

    private Event mockEvent;
    private StripeObject mockStripeObject;

    public void setShouldThrowParsingException(boolean shouldThrow) {
        this.shouldThrowParsingException = shouldThrow;
    }

    public void setShouldThrowSignatureException(boolean shouldThrow) {
        this.shouldThrowSignatureException = shouldThrow;
    }

    public void setShouldThrowObjectExtractionException(boolean shouldThrow) {
        this.shouldThrowObjectExtractionException = shouldThrow;
    }

    public void setMockEvent(Event event) {
        this.mockEvent = event;
    }

    public void setMockStripeObject(StripeObject stripeObject) {
        this.mockStripeObject = stripeObject;
    }

    @Override
    public Event parseEvent(String payload) {
        if (shouldThrowParsingException) {
            throw new WebhookParsingFailedException();
        }
        return mockEvent;
    }

    @Override
    public Event verifySignature(String payload, String sigHeader) {
        if (shouldThrowSignatureException) {
            throw new WebhookSignatureVerificationFailedException();
        }
        return mockEvent;
    }

    @Override
    public StripeObject extractStripeObject(Event event) {
        if (shouldThrowObjectExtractionException) {
            throw new WebhookObjectExtractionFailedException();
        }
        return mockStripeObject;
    }
} 