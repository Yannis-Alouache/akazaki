package com.akazaki.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// https://docs.stripe.com/api/payment_intents
// https://docs.stripe.com/payments/payment-intents
// https://docs.stripe.com/payments/accept-a-payment?ui=elements

// TODO: Add decrease stock when order is created
// TODO: Add custom exception for order use case
@SpringBootApplication
public class BackendApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}
}
