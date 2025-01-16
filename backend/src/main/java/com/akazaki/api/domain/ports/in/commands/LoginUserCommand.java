package com.akazaki.api.domain.ports.in.commands;

public record LoginUserCommand(
        String email,
        String password
) {
}