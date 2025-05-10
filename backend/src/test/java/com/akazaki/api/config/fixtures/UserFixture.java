package com.akazaki.api.config.fixtures;

import com.akazaki.api.domain.model.User;

public class UserFixture {
    public static final User basicUser = User.create(null, "Doe", "John", "johndoe@akazaki.com", "encodedPassword", "0685357448", false);
    public static final User adminUser = User.create(null, "Smith", "Admin", "admin@akazaki.com", "encodedPassword", "0712345678", true);
}