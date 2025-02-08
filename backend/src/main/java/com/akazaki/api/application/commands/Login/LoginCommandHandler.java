package com.akazaki.api.application.commands.Login;

import com.akazaki.api.domain.exceptions.InvalidCredentialsException;
import com.akazaki.api.domain.ports.in.commands.LoginUserCommand;
import com.akazaki.api.domain.ports.out.UserRepository;
import com.akazaki.api.infrastructure.security.JwtService;
import com.akazaki.api.infrastructure.web.dto.auth.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoginCommandHandler {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponse handle(LoginUserCommand command) {
        var user = userRepository.findByEmail(command.email())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(command.password(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        return new LoginResponse(jwtService.generateToken(user));
    }
} 