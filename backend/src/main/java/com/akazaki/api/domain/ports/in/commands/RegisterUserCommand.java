package com.akazaki.api.domain.ports.in.commands;

import com.akazaki.api.domain.model.User;

public record RegisterUserCommand(
        String email,
        String password,
        String firstName,
        String lastName,
        String phoneNumber,
        boolean admin
) {}