package com.akazaki.api.domain.ports.out;

import com.stripe.model.Event;
import com.stripe.model.StripeObject;

public interface WebhookGateway {
    Event parseEvent(String payload);
    Event verifySignature(String payload, String sigHeader);
    StripeObject extractStripeObject(Event event);
}
