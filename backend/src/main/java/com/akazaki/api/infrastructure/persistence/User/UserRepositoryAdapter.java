package com.akazaki.api.infrastructure.persistence.User;

import com.akazaki.api.domain.model.User;
import com.akazaki.api.domain.ports.out.UserRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("prod")
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final JpaUserRepository repository;
    private final UserPersistenceMapper mapper;

    @Override
    public User save(User user) {
        UserEntity userEntity = mapper.toEntity(user);
        UserEntity savedUserEntity = repository.save(userEntity);
        return mapper.toDomain(savedUserEntity);
    }

    @Override
    public Optional<User> findById(Long id) {
        Optional<UserEntity> userEntity = repository.findById(id);
        return userEntity.map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        Optional<UserEntity> userEntity = repository.findByEmail(email);
        return userEntity.map(mapper::toDomain);
    }

    @Override
    public boolean exists(String email) {
        boolean exists = repository.existsByEmail(email);
        return exists;
    }

    @Override
    public List<User> findAll() {
        List<UserEntity> userEntities = repository.findAll();
        return mapper.toDomainList(userEntities);
    }
}
