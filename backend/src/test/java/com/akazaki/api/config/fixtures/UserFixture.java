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

    public final User basicUser = new User(1L, "Doe", "John", "johndoe@akazaki.com", "johndoe1234", "0685357448", false);
    public final User adminUser = new User(2L, "Smith", "Admin", "admin@akazaki.com", "adminPass123", "0712345678", true);


    public List<User> saveUsers() {
        List<User> users = new ArrayList<>();

        users.add(basicUser);
        users.add(adminUser);

        
        User admin = userRepository.save(adminUser);
        User user = userRepository.save(basicUser);

        System.out.println("user1 = " + user);
        System.out.println("user2 = " + admin);


        User test1 = userRepository.findById(1L).orElseThrow();
        User test2 = userRepository.findById(2L).orElseThrow();

        System.out.println("user111111 = " + test1);
        System.out.println("user222222 = " + test2);

        return users;
    }
}
