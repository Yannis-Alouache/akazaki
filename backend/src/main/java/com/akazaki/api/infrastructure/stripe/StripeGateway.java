package com.akazaki.api.infrastructure.stripe;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.stereotype.Component;
import com.akazaki.api.domain.model.Order;
import com.akazaki.api.domain.model.OrderItem;
import com.akazaki.api.domain.ports.out.PaymentGateway;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StripeGateway implements PaymentGateway { // TODO: To Test

    private static final String RETURN_URL = "https://your-frontend.com/checkout/success?session_id={CHECKOUT_SESSION_ID}";

    @Override
    public String createCheckoutSession(Order order) {
        List<SessionCreateParams.LineItem> lineItems = order.getItems().stream()
            .map(this::mapToLineItem)
            .collect(Collectors.toList());

        SessionCreateParams.Builder paramsBuilder = SessionCreateParams.builder()
            .setMode(SessionCreateParams.Mode.PAYMENT)
            .setUiMode(SessionCreateParams.UiMode.CUSTOM)
            .setReturnUrl(RETURN_URL);

        lineItems.forEach(paramsBuilder::addLineItem);

        try {
            Session session = Session.create(paramsBuilder.build());
            return session.getClientSecret();
        } catch (StripeException e) {
            throw new RuntimeException("Stripe checkout session failed", e);
        }
    }

    private SessionCreateParams.LineItem mapToLineItem(OrderItem orderItem) {
        return SessionCreateParams.LineItem.builder()
            .setQuantity((long) orderItem.getQuantity())
            .setPriceData(
                SessionCreateParams.LineItem.PriceData.builder()
                    .setCurrency("eur") // TODO: rendre ça dynamique
                    .setUnitAmount(convertToCents(orderItem.getPrice())) // TODO: check that orderItem.getPrice() is total price instead of unit price
                    .setProductData(
                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                            .setName(orderItem.getProduct().getName())
                            .setDescription(orderItem.getProduct().getDescription())
                            .addImage(orderItem.getProduct().getImageUrl())
                            .build()
                    )
                    .build()
            )
            .build();
    }

    private long convertToCents(double price) {
        return (long) price * 100;
    }
}
