package com.akazaki.api.application.queries;

import com.akazaki.api.domain.exceptions.UserNotFoundException;
import com.akazaki.api.domain.model.User;
import com.akazaki.api.domain.ports.in.queries.GetUserQuery;
import com.akazaki.api.domain.ports.out.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class GetUserQueryHandler {
    private final UserRepository userRepository;

    public GetUserQueryHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User handle(GetUserQuery query) {
        return userRepository.findById(query.getUserId())
            .orElseThrow(() -> new UserNotFoundException(query.getUserId()));
    }
} 