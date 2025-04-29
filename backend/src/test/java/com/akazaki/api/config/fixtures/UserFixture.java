package com.akazaki.api.config.fixtures;

import com.akazaki.api.domain.model.User;
import com.akazaki.api.domain.ports.out.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;


@Component
public class UserFixture {

    @Autowired
    private UserRepository userRepository;

    public final User basicUser = User.create(null, "Doe", "John", "johndoe@akazaki.com", "johndoe1234", "0685357448", false);
    public final User adminUser = User.create(null, "Smith", "Admin", "admin@akazaki.com", "adminPass123", "0712345678", true);

    public UserFixture() {}

    public UserFixture(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> saveUsers() {
        List<User> users = new ArrayList<>();

        users.add(basicUser);
        users.add(adminUser);
        
        userRepository.save(adminUser);
        userRepository.save(basicUser);
        
        return users;
    }
}
