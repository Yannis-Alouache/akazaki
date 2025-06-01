package com.akazaki.api.infrastructure.stripe;

import org.springframework.stereotype.Component;
import com.akazaki.api.domain.model.PaymentMethodEnum;

@Component
public class StripePaymentMethodMapper {
    public PaymentMethodEnum toDomainEnum(String stripePaymentMethodType) {
        return switch (stripePaymentMethodType.toLowerCase()) {
            case "card" -> PaymentMethodEnum.CREDIT_CARD;
            case "paypal" -> PaymentMethodEnum.PAYPAL;
            default -> PaymentMethodEnum.OTHER;
        };
    }
} 