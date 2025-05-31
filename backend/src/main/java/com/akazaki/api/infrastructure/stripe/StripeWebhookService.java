package com.akazaki.api.infrastructure.stripe;

import org.springframework.beans.factory.annotation.Value;

import com.google.gson.JsonSyntaxException;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.StripeObject;
import com.stripe.net.ApiResource;
import com.stripe.net.Webhook;

public class StripeWebhookService {

    @Value("whsec_b2148f99137f118d81a59b2cce99e2561c1003f5b9a9e6f4572706d9d92e5498")
    private String endpointSecret;

    public Event parseEvent(String payload) {
        try {
            Event event = ApiResource.GSON.fromJson(payload, Event.class);
            return event;
        } catch (JsonSyntaxException e) {
            throw new RuntimeException("⚠️  Webhook error while parsing basic request.");
        }
    }

    public Event verifySignature(String payload, String sigHeader) {
        try {
            Event event = Webhook.constructEvent(
                payload, sigHeader, endpointSecret
            );
            return event;
        } catch (SignatureVerificationException e) {
            throw new RuntimeException("⚠️  Webhook error while validating signature.");
        }
    }

    public StripeObject extractStripeObject(Event event) {
        EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();
        
        return deserializer.getObject().orElseThrow(
            () -> new RuntimeException("⚠️  Webhook error while extracting stripe object.")
        );
    }

    
    
}
