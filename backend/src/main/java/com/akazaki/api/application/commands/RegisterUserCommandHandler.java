package com.akazaki.api.application.commands;

import com.akazaki.api.domain.exceptions.EmailAlreadyRegisteredException;
import com.akazaki.api.domain.model.User;
import com.akazaki.api.domain.ports.in.commands.RegisterUserCommand;
import com.akazaki.api.domain.ports.out.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegisterUserCommandHandler {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public User handle(RegisterUserCommand command) {
        if (userRepository.exists(command.email())) {
            throw new EmailAlreadyRegisteredException();
        }

        User user = User.builder()
            .email(command.email())
            .password(passwordEncoder.encode(command.password()))
            .firstName(command.firstName())
            .lastName(command.lastName())
            .phoneNumber(command.phoneNumber())
            .admin(command.admin())
            .build();
        
        return userRepository.save(user);
    }
} 