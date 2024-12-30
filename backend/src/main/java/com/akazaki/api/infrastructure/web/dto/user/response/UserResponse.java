package com.akazaki.api.infrastructure.web.dto.user.response;

public record UserResponse(
    Long id,
    String email,
    String firstName,
    String lastName,
    String phoneNumber,
    boolean admin
) {}