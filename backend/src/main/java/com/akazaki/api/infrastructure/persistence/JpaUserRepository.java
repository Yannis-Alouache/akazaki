package com.akazaki.api.infrastructure.persistence;

import com.akazaki.api.domain.model.User;
import com.akazaki.api.domain.ports.out.UserRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.context.annotation.Profile;

import java.util.Optional;

@Repository
@Profile("prod")
public interface JpaUserRepository extends CrudRepository<User, Long>, UserRepository {
    boolean existsByEmail(String email);

    @Override
    default boolean exists(String email) {
        return existsByEmail(email);
    }
}