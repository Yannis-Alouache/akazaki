package com.akazaki.api.domain.ports.in.commands;

public record RegisterUserCommand(
        String email,
        String password,
        String firstName,
        String lastName,
        String phoneNumber,
        boolean admin
) {}