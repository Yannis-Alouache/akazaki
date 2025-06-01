package com.akazaki.api.infrastructure.stripe;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.param.PaymentIntentCreateParams;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.akazaki.api.domain.model.Order;
import com.akazaki.api.domain.model.PaymentMethodEnum;
import com.akazaki.api.domain.ports.out.PaymentGateway;


@Component
public class StripePaymentIntentsGateway implements PaymentGateway {

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;
    
    private final StripePaymentMethodMapper paymentMethodMapper;
    
    public StripePaymentIntentsGateway(StripePaymentMethodMapper paymentMethodMapper) {
        this.paymentMethodMapper = paymentMethodMapper;
    }

    @Override
    public String createPaymentIntent(Order order) {
        Stripe.apiKey = stripeSecretKey;

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
                .putMetadata("user_id", order.getUser().getId().toString())
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

    @Override
    public PaymentMethodEnum getPaymentMethod(String paymentMethodId) {
        try {
            PaymentMethod paymentMethod = PaymentMethod.retrieve(paymentMethodId);
            return paymentMethodMapper.toDomainEnum(paymentMethod.getType());
        } catch (StripeException e) {
            throw new RuntimeException("Payment Method retrieval failed", e);
        }
    }
}