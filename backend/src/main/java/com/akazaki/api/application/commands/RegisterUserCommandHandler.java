package com.akazaki.api.application.commands;

import com.akazaki.api.domain.exceptions.EmailAlreadyRegisteredException;
import com.akazaki.api.domain.model.User;
import com.akazaki.api.domain.ports.in.commands.RegisterUserCommand;
import com.akazaki.api.domain.ports.out.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class RegisterUserCommandHandler {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public RegisterUserCommandHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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