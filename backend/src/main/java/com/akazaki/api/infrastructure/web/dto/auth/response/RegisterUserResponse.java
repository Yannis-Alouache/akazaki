package com.akazaki.api.infrastructure.web.dto.auth.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User registration response information")
public record RegisterUserResponse (
    @Schema(description = "User ID", example = "123")
    Long id,
    
    @Schema(description = "User email", example = "john.doe@example.com")
    String email,
    
    @Schema(description = "First name", example = "John")
    String firstName,
    
    @Schema(description = "Last name", example = "Doe")
    String lastName,
    
    @Schema(description = "Phone number", example = "+33612345678")
    String phoneNumber,
    
    @Schema(description = "Admin privileges", example = "false")
    boolean admin
) {}