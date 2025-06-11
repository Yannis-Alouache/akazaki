package com.akazaki.api.infrastructure.web.controller.webhook;

import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.Stripe;
import com.stripe.model.Event;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.akazaki.api.application.commands.CreatePaymentCommandHandler;
import com.akazaki.api.application.commands.DecreaseProductStockCommandHandler;
import com.akazaki.api.application.commands.MarkOrderAsPaidCommandHandler;
import com.akazaki.api.domain.ports.in.commands.CreatePaymentCommand;
import com.akazaki.api.domain.ports.in.commands.DecreaseProductStockCommand;
import com.akazaki.api.domain.ports.in.commands.MarkOrderAsPaidCommand;
import com.akazaki.api.infrastructure.stripe.StripeWebhookGateway;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/")
@Tag(name = "Webhook", description = "External service webhook handlers")
public class WebhookController {

    @Value("whsec_b2148f99137f118d81a59b2cce99e2561c1003f5b9a9e6f4572706d9d92e5498")
    private String endpointSecret;

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    private final StripeWebhookGateway stripeWebhookGateway;
    private final MarkOrderAsPaidCommandHandler markOrderAsPaidCommandHandler;
    private final CreatePaymentCommandHandler createPaymentCommandHandler;
    private final DecreaseProductStockCommandHandler decreaseProductStockCommandHandler;

    public WebhookController(StripeWebhookGateway stripeWebhookGateway, MarkOrderAsPaidCommandHandler markOrderAsPaidCommandHandler, CreatePaymentCommandHandler createPaymentCommandHandler, DecreaseProductStockCommandHandler decreaseProductStockCommandHandler) {
        this.stripeWebhookGateway = stripeWebhookGateway;
        this.markOrderAsPaidCommandHandler = markOrderAsPaidCommandHandler;
        this.createPaymentCommandHandler = createPaymentCommandHandler;
        this.decreaseProductStockCommandHandler = decreaseProductStockCommandHandler;
    }
   
    @Operation(
        summary = "Handle Stripe webhook events",
        description = "Processes webhook events from Stripe, particularly payment_intent.succeeded events to complete order processing"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Webhook event successfully processed"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid webhook signature or payload"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error processing webhook"
        )
    })
    @PostMapping("/stripe/webhook")
    public ResponseEntity<String> handleStripeWebhook(
        @RequestBody String payload,
        @RequestHeader("Stripe-Signature") String sigHeader
    ) {
            Stripe.apiKey = stripeSecretKey;
            Event event = stripeWebhookGateway.parseEvent(payload);

            if(endpointSecret != null && sigHeader != null)
                event = stripeWebhookGateway.verifySignature(payload, sigHeader);

            StripeObject stripeObject = stripeWebhookGateway.extractStripeObject(event);

            // Handle the event
            switch (event.getType()) {
                case "payment_intent.succeeded":
                    PaymentIntent paymentIntent = (PaymentIntent) stripeObject;
                    Long orderId = Long.parseLong(paymentIntent.getMetadata().get("orderId"));
                    String paymentMethodId = paymentIntent.getPaymentMethod();

                    markOrderAsPaidCommandHandler.handle(
                        new MarkOrderAsPaidCommand(orderId)
                    );
                    createPaymentCommandHandler.handle(
                        new CreatePaymentCommand(orderId, paymentMethodId)
                    );
                    // Decrease stock of ordered products when payment succeeds
                    decreaseProductStockCommandHandler.handle(
                        new DecreaseProductStockCommand(orderId)
                    );
                break;
                default:
                    System.out.println("Unhandled event type: " + event.getType());
                break;
            }
            return ResponseEntity.ok("OK");
    }
}