package com.akazaki.api.infrastructure.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User response information")
public record UserResponse(
    @Schema(description = "User ID", example = "123")
    Long id,
    
    @Schema(description = "User email", example = "jean.dupont@example.com")
    String email,
    
    @Schema(description = "First name", example = "Jean")
    String firstName,
    
    @Schema(description = "Last name", example = "Dupont")
    String lastName,
    
    @Schema(description = "Phone number", example = "+33123456789")
    String phoneNumber,
    
    @Schema(description = "Admin privileges", example = "false")
    boolean admin
) {}