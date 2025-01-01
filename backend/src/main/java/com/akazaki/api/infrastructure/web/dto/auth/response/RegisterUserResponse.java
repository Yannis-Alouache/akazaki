package com.akazaki.api.infrastructure.web.dto.auth.response;

public record RegisterUserResponse (
    Long id,
    String email,
    String firstName,
    String lastName,
    String phoneNumber,
    boolean admin
) {}