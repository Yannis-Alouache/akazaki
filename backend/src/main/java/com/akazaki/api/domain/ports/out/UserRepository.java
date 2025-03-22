package com.akazaki.api.domain.ports.out;

import com.akazaki.api.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);
    boolean exists(String email);
    List<User> findAll();
}