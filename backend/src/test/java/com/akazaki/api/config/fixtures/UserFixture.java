package com.akazaki.api.config.fixtures;

import com.akazaki.api.domain.model.User;
import com.akazaki.api.domain.ports.out.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserFixture {
    private final UserRepository userRepository;

    public User basicUser;
    public User adminUser;

    public UserFixture(UserRepository userRepository) {
        this.userRepository = userRepository;

        basicUser = User.create(null, "Doe", "John", "johndoe@akazaki.com", "johndoe1234", "0685357448", false);
        adminUser = User.create(null, "Smith", "Admin", "admin@akazaki.com", "adminPass123", "0712345678", true);
    }

    public void saveUsers() {
        this.basicUser = userRepository.save(adminUser);
        this.adminUser = userRepository.save(basicUser);
    }
}
