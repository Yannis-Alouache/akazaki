package com.akazaki.api.application.queries.GetAllUsers;

import com.akazaki.api.domain.model.User;
import com.akazaki.api.domain.ports.in.queries.GetAllUsersQuery;
import com.akazaki.api.domain.ports.out.UserRepository;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class GetAllUsersQueryHandler {
    private final UserRepository userRepository;

    public GetAllUsersQueryHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> handle(GetAllUsersQuery query) {
        return userRepository.findAll();
    }
}