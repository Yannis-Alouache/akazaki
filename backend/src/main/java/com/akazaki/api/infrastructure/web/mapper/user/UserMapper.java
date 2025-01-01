package com.akazaki.api.infrastructure.web.mapper.user;

import com.akazaki.api.domain.model.User;
import com.akazaki.api.infrastructure.web.dto.user.response.UserResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

@Component
public class UserMapper {
    
    public UserResponse toResponse(User user) {
        return new UserResponse(
            user.getId(),
            user.getEmail(),
            user.getFirstName(),
            user.getLastName(),
            user.getPhoneNumber(),
            user.isAdmin()
        );
    }

    public List<UserResponse> toResponseList(Iterable<User> users) {
        List<UserResponse> responses = new ArrayList<>();
        users.forEach(user -> responses.add(toResponse(user)));
        return responses;
    }
} 