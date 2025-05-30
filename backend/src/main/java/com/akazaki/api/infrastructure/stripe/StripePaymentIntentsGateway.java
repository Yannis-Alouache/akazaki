package com.akazaki.api.infrastructure.stripe;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.stereotype.Component;
import com.akazaki.api.domain.model.Order;
import com.akazaki.api.domain.ports.out.PaymentGateway;


@Component
public class StripePaymentIntentsGateway implements PaymentGateway {

    @Override
    public String createPaymentIntent(Order order) {
        try {
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(calculateTotalInCents(order))
                .setCurrency("eur")
                .setAutomaticPaymentMethods(
                    PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                        .setEnabled(true)
                        .build()
                )

                .putMetadata("order_id", order.getId().toString())
                .putMetadata("user_id", "1") // TODO: Mettre à jour avec l'id de l'utilisateur
                .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);
            return paymentIntent.getClientSecret();
        } catch (StripeException e) {
            throw new RuntimeException("Payment Intent creation failed", e);
        }
    }
    
    private Long calculateTotalInCents(Order order) {
        double total = order.getTotalPrice();
        return (long) (total * 100);
    }
}