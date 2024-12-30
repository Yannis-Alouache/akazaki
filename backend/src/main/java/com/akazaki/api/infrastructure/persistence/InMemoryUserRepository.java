package com.akazaki.api.infrastructure.persistence;

import com.akazaki.api.domain.model.User;
import com.akazaki.api.domain.ports.out.UserRepository;
import org.springframework.stereotype.Repository;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("test")
public class InMemoryUserRepository implements UserRepository {
    private final List<User> users = new ArrayList<>();

    @Override
    public User save(User user) {
        users.add(user);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return users.stream()
            .filter(user -> user.getId().equals(id))
            .findFirst();
    }

    @Override
    public boolean exists(String email) {
        return users.stream()
            .anyMatch(user -> user.getEmail().equals(email));
    }

    @Override
    public Iterable<User> findAll() {
        return users;
    }
} 