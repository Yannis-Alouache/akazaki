package com.akazaki.api.infrastructure.web.dto.response;

public record AddressResponse(
    Long id,
    String lastName,
    String firstName,
    String streetNumber,
    String street,
    String addressComplement,
    String postCode,
    String city,
    String country
) {}