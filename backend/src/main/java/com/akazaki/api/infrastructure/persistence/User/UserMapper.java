package com.akazaki.api.infrastructure.persistence.User;

import java.util.List;

import org.springframework.stereotype.Component;

import com.akazaki.api.domain.model.User;

import lombok.RequiredArgsConstructor;

@Component(value = "DomainUserMapper")
@RequiredArgsConstructor
public class UserMapper {
    public UserEntity toEntity(User domain) {
        return new UserEntity(
                domain.getId(),
                domain.getLastName(),
                domain.getFirstName(),
                domain.getEmail(),
                domain.getPassword(),
                domain.getPhoneNumber(),
                domain.isAdmin()
        );
    }

    public User toDomain(UserEntity savedUserEntity) {
        return User.create(
                savedUserEntity.getId(),
                savedUserEntity.getLastName(),
                savedUserEntity.getFirstName(),
                savedUserEntity.getEmail(),
                savedUserEntity.getPassword(),
                savedUserEntity.getPhoneNumber(),
                savedUserEntity.isAdmin()
        );
    }

    public List<User> toDomainList(List<UserEntity> userEntities) {
        return userEntities.stream()
                .map(this::toDomain)
                .toList();
    }
}
