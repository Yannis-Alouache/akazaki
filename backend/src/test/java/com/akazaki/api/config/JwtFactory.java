package com.akazaki.api.config;

import org.springframework.beans.factory.annotation.Autowired;

import com.akazaki.api.domain.model.User;
import com.akazaki.api.infrastructure.security.JwtService;
import org.springframework.stereotype.Component;

@Component
public class JwtFactory {
    @Autowired
    private JwtService jwtService;

    public String getJwtToken(User user) {
        return jwtService.generateToken(user);
    }
}
