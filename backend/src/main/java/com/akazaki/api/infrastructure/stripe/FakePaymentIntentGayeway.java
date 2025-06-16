package com.akazaki.api.infrastructure.stripe;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.akazaki.api.domain.model.Order;
import com.akazaki.api.domain.model.PaymentMethodEnum;
import com.akazaki.api.domain.ports.out.PaymentGateway;
import com.akazaki.api.infrastructure.exceptions.PaymentIntentCreationFailedException;
import com.akazaki.api.infrastructure.exceptions.PaymentMethodRetrievalFailedException;

@Component
@Profile("test")
public class FakePaymentIntentGayeway implements PaymentGateway {

    private boolean shouldFailOnCreatePaymentIntent = false;
    private boolean shouldFailOnGetPaymentMethod = false;
    private final Map<String, PaymentMethodEnum> paymentMethods = new HashMap<>();

    @Override
    public String createPaymentIntent(Order order) {
        if (shouldFailOnCreatePaymentIntent) {
            throw new PaymentIntentCreationFailedException();
        }

        return "payment_intent_id";
    }

    @Override
    public PaymentMethodEnum getPaymentMethod(String paymentMethodId) {
        if (shouldFailOnGetPaymentMethod) {
            throw new PaymentMethodRetrievalFailedException();
        }

        return PaymentMethodEnum.CREDIT_CARD;
    }

    public void setupPaymentMethods(String paymentMethodId, PaymentMethodEnum paymentMethod) {
        this.paymentMethods.put(paymentMethodId, paymentMethod);
    }

    public void simulatePaymentIntentCreationFailure() {
        shouldFailOnCreatePaymentIntent = true;
    }

    public void simulatePaymentMethodRetrievalFailure() {
        shouldFailOnGetPaymentMethod = true;
    }
    
}
