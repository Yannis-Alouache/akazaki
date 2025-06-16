package com.akazaki.api.infrastructure.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Address response information")
public record AddressResponse(
    @Schema(description = "Address ID", example = "456")
    Long id,
    
    @Schema(description = "Last name", example = "Dupont")
    String lastName,
    
    @Schema(description = "First name", example = "Jean")
    String firstName,
    
    @Schema(description = "Street number", example = "123")
    String streetNumber,
    
    @Schema(description = "Street name", example = "Rue de la Paix")
    String street,
    
    @Schema(description = "Address complement", example = "Apt 4B")
    String addressComplement,
    
    @Schema(description = "Postal code", example = "75001")
    String postCode,
    
    @Schema(description = "City", example = "Paris")
    String city,
    
    @Schema(description = "Country", example = "France")
    String country
) {}