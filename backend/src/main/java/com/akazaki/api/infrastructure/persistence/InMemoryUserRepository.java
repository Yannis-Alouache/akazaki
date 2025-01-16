package com.akazaki.api.infrastructure.persistence;

import com.akazaki.api.domain.model.User;
import com.akazaki.api.domain.ports.out.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("test")
public class InMemoryUserRepository implements UserRepository {
    private final List<User> users = new ArrayList<>();
    private static final Logger logger = LoggerFactory.getLogger(InMemoryUserRepository.class);

    @Override
    public User save(User user) {
        user.setId((long) (users.size() + 1));
        users.add(user);
        logger.debug("trying to save user : {}", user);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return users.stream()
            .filter(user -> user.getId().equals(id))
            .findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return users.stream()
                .filter(user -> user.getEmail().equals(email))
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