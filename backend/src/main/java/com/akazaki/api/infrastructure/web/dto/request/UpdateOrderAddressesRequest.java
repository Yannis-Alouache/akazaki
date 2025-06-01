package com.akazaki.api.infrastructure.web.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record UpdateOrderAddressesRequest(
    @NotNull(message = "L'adresse de facturation est obligatoire")
    @Valid
    AddressRequest billingAddress,
    
    @NotNull(message = "L'adresse de livraison est obligatoire") 
    @Valid
    AddressRequest shippingAddress
) {} 