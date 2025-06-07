package com.akazaki.api.infrastructure.stripe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.akazaki.api.infrastructure.exceptions.WebhookObjectExtractionFailedException;
import com.akazaki.api.infrastructure.exceptions.WebhookParsingFailedException;
import com.akazaki.api.infrastructure.exceptions.WebhookSignatureVerificationFailedException;
import com.google.gson.JsonSyntaxException;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.StripeObject;
import com.stripe.net.ApiResource;
import com.stripe.net.Webhook;

@Service
public class StripeWebhookService {

    @Value("whsec_b2148f99137f118d81a59b2cce99e2561c1003f5b9a9e6f4572706d9d92e5498")
    private String endpointSecret;
    private final Logger log = LoggerFactory.getLogger(StripeWebhookService.class);
    
    public Event parseEvent(String payload) {
        try {
            Event event = ApiResource.GSON.fromJson(payload, Event.class);
            return event;
        } catch (JsonSyntaxException e) {
            log.error("Webhook error while parsing basic request.", e);
            throw new WebhookParsingFailedException();
        }
    }

    public Event verifySignature(String payload, String sigHeader) {
        try {
            Event event = Webhook.constructEvent(
                payload, sigHeader, endpointSecret
            );
            return event;
        } catch (SignatureVerificationException e) {
            log.error("Webhook error while validating signature.", e);
            throw new WebhookSignatureVerificationFailedException();
        }
    }

    public StripeObject extractStripeObject(Event event) {
        EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();

        return deserializer.getObject().orElseThrow(WebhookObjectExtractionFailedException::new);
    }
}
