package com.akazaki.api.infrastructure.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Request for address information")
public record AddressRequest(
    @Schema(description = "Last name", example = "Dupont")
    @NotNull(message = "Le nom de famille est obligatoire")
    @NotBlank(message = "Le nom de famille ne peut pas être vide")
    @Size(max = 50, message = "Le nom de famille ne peut pas dépasser 50 caractères")
    String lastName,
    
    @Schema(description = "First name", example = "Jean")
    @NotNull(message = "Le prénom est obligatoire")
    @NotBlank(message = "Le prénom ne peut pas être vide")
    @Size(max = 50, message = "Le prénom ne peut pas dépasser 50 caractères")
    String firstName,
    
    @Schema(description = "Street number", example = "123")
    @NotNull(message = "Le numéro de rue est obligatoire")
    @NotBlank(message = "Le numéro de rue ne peut pas être vide")
    @Size(max = 10, message = "Le numéro de rue ne peut pas dépasser 10 caractères")
    String streetNumber,
    
    @Schema(description = "Street name", example = "Rue de la Paix")
    @NotNull(message = "Le nom de rue est obligatoire")
    @NotBlank(message = "Le nom de rue ne peut pas être vide")
    @Size(max = 100, message = "Le nom de rue ne peut pas dépasser 100 caractères")
    String street,
    
    @Schema(description = "Address complement (optional)", example = "Apt 4B")
    @Size(max = 100, message = "Le complément d'adresse ne peut pas dépasser 100 caractères")
    String addressComplement,
    
    @Schema(description = "Postal code", example = "75001")
    @NotNull(message = "Le code postal est obligatoire")
    @NotBlank(message = "Le code postal ne peut pas être vide")
    @Pattern(regexp = "^[0-9]{5}$", message = "Le code postal doit contenir exactement 5 chiffres")
    String postalCode,
    
    @Schema(description = "City", example = "Paris")
    @NotNull(message = "La ville est obligatoire")
    @NotBlank(message = "La ville ne peut pas être vide")
    @Size(max = 50, message = "La ville ne peut pas dépasser 50 caractères")
    String city,
    
    @Schema(description = "Country", example = "France")
    @NotNull(message = "Le pays est obligatoire")
    @NotBlank(message = "Le pays ne peut pas être vide")
    @Size(max = 50, message = "Le pays ne peut pas dépasser 50 caractères")
    String country
) {} 