package com.akazaki.api.infrastructure.web.dto.auth.request;

import com.akazaki.api.infrastructure.validation.PhoneNumber;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Request object for user registration")
public class RegisterUserRequest {
    @NotBlank(message = "L'adresse email est obligatoire")
    @Email(message = "Format d'email invalide")
    @Schema(description = "User's email address", example = "john.doe@example.com")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    @Schema(description = "User's password", example = "securePassword123")
    private String password;

    @NotBlank(message = "Le prénom est obligatoire")
    @Schema(description = "User's first name", example = "John")
    private String firstName;

    @NotBlank(message = "Le nom est obligatoire")
    @Schema(description = "User's last name", example = "Doe")
    private String lastName;

    @NotBlank(message = "Le numéro de téléphone est obligatoire")
    @PhoneNumber(message = "Le numéro de téléphone doit être un numéro français valide (ex: +33612345678 ou 0612345678)")
    @Schema(description = "User's phone number", example = "+33612345678")
    private String phoneNumber;

    @Schema(description = "Whether the user has admin privileges", example = "false")
    private boolean admin;
} 