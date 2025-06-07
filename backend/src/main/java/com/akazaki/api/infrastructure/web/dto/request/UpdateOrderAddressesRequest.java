package com.akazaki.api.infrastructure.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request to update order addresses")
public record UpdateOrderAddressesRequest(
    @Schema(description = "Billing address information", implementation = AddressRequest.class)
    @NotNull(message = "L'adresse de facturation est obligatoire")
    @Valid
    AddressRequest billingAddress,
    
    @Schema(description = "Shipping address information", implementation = AddressRequest.class)
    @NotNull(message = "L'adresse de livraison est obligatoire") 
    @Valid
    AddressRequest shippingAddress
) {} 