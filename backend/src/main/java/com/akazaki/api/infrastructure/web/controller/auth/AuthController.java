package com.akazaki.api.infrastructure.web.controller.auth;

import com.akazaki.api.application.commands.Login.LoginCommandHandler;
import com.akazaki.api.application.commands.Register.RegisterUserCommandHandler;
import com.akazaki.api.domain.model.User;
import com.akazaki.api.domain.ports.in.commands.LoginUserCommand;
import com.akazaki.api.domain.ports.in.commands.RegisterUserCommand;
import com.akazaki.api.infrastructure.web.dto.auth.request.LoginRequest;
import com.akazaki.api.infrastructure.web.dto.auth.request.RegisterUserRequest;
import com.akazaki.api.infrastructure.web.dto.auth.response.LoginResponse;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    private final RegisterUserCommandHandler registerUserCommandHandler;
    private final LoginCommandHandler loginCommandHandler;
    private final AuthMapper authMapper;

    public AuthController(
            RegisterUserCommandHandler registerUserCommandHandler,
            LoginCommandHandler loginCommandHandler,
            AuthMapper authMapper) {
        this.registerUserCommandHandler = registerUserCommandHandler;
        this.loginCommandHandler = loginCommandHandler;
        this.authMapper = authMapper;
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
        RegisterUserCommand command = authMapper.toCommand(request);
        User user = registerUserCommandHandler.handle(command);
        return ResponseEntity.ok(authMapper.toResponse(user));
    }

    @Operation(
            summary = "Login user",
            description = "Authenticate a user and return a JWT token"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User successfully authenticated",
                    content = @Content(schema = @Schema(implementation = LoginResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid credentials",
                    content = @Content
            )
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        logger.info("Received login request for email: {}", request.getEmail());
        LoginUserCommand command = new LoginUserCommand(request.getEmail(), request.getPassword());
        LoginResponse response = loginCommandHandler.handle(command);
        return ResponseEntity.ok(response);
    }
} 