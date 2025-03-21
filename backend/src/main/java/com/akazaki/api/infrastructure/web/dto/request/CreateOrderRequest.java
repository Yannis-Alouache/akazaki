package com.akazaki.api.infrastructure.web.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {

    @NotNull(message = "Order date is required")
    private LocalDateTime date;

    @NotBlank(message = "Order status is required")
    private String status;

    @Min(value = 0, message = "Total price must be greater than or equal to 0")
    private int totalPrice;

    @NotNull(message = "Billing address is required")
    private String billingAddress;

    @NotNull(message = "Shipping address is required")
    private String shippingAddress;

}
