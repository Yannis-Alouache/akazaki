package com.akazaki.api.infrastructure.web.controller.auth;

import com.akazaki.api.application.commands.RegisterUserCommandHandler;
import com.akazaki.api.infrastructure.web.dto.auth.request.RegisterUserRequest;
import com.akazaki.api.infrastructure.web.dto.auth.response.RegisterUserResponse;
import com.akazaki.api.infrastructure.web.mapper.auth.AuthMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    private final RegisterUserCommandHandler registerUserCommandHandler;
    private final AuthMapper authMapper;

    public AuthController(
            RegisterUserCommandHandler registerUserCommandHandler,
            AuthMapper authMapper) {
        this.registerUserCommandHandler = registerUserCommandHandler;
        this.authMapper = authMapper;
        logger.info("AuthController initialized");
    }

    @Operation(
        summary = "Register a new user",
        description = "Creates a new user account with the provided information"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "User successfully registered",
            content = @Content(schema = @Schema(implementation = RegisterUserResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input or email already exists",
            content = @Content
        )
    })
    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponse> register(@Valid @RequestBody RegisterUserRequest request) {
        logger.info("Received register request for email: {}", request.getEmail());
        var command = authMapper.toCommand(request);
        var user = registerUserCommandHandler.handle(command);
        return ResponseEntity.ok(authMapper.toResponse(user));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }
} 