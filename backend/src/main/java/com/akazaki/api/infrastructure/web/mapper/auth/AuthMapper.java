package com.akazaki.api.infrastructure.web.mapper.auth;

import com.akazaki.api.domain.model.User;
import com.akazaki.api.domain.ports.in.commands.RegisterUserCommand;
import com.akazaki.api.infrastructure.web.dto.auth.request.RegisterUserRequest;
import com.akazaki.api.infrastructure.web.dto.auth.response.RegisterUserResponse;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {
    
    public RegisterUserCommand toCommand(RegisterUserRequest request) {
        return new RegisterUserCommand(
            request.getEmail(),
            request.getPassword(),
            request.getFirstName(),
            request.getLastName(),
            request.getPhoneNumber(),
            request.isAdmin()
        );
    }
    
    public RegisterUserResponse toResponse(User user) {
        return new RegisterUserResponse(
            user.getId(),
            user.getEmail(),
            user.getFirstName(),
            user.getLastName(),
            user.getPhoneNumber(),
            user.isAdmin()
        );
    }
} 