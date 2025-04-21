package com.akazaki.api.application.commands.RegisterUser;

import com.akazaki.api.application.commands.Register.RegisterUserCommandHandler;
import com.akazaki.api.domain.exceptions.EmailAlreadyRegisteredException;
import com.akazaki.api.domain.model.User;
import com.akazaki.api.domain.ports.in.commands.RegisterUserCommand;
import com.akazaki.api.infrastructure.persistence.User.InMemoryUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class RegisterUserUnitTest {

    private RegisterUserCommandHandler handler;
    private InMemoryUserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;
    private User user;

    @BeforeEach
    void setUp() {
        repository = new InMemoryUserRepository();
        handler = new RegisterUserCommandHandler(repository, passwordEncoder);

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        
        user = User.create(
            1L,
            "Doe",
            "John",
            "hello@exemple.com",
            "encodedPassword",
            "0685357448",
            false
        );
    }

    @Test
    void createAUser() {
        RegisterUserCommand command = new RegisterUserCommand(
                "hello@exemple.com",
                "123456",
                "John",
                "Doe",
                "0685357448",
                false
        );

        User result = handler.handle(command);
        assertThat(result).usingRecursiveComparison().isEqualTo(user);
        assertThat(repository.exists(result.getEmail())).isTrue();
    }

    @Test
    void preventDuplicateUser() {
        RegisterUserCommand command = new RegisterUserCommand(
                "hello@exemple.com",
                "123456",
                "John",
                "Doe",
                "0685357448",
                false
        );

        // First creation should succeed
        handler.handle(command);

        // When/Then - Second creation should fail
        assertThatThrownBy(() -> handler.handle(command))
                .isInstanceOf(EmailAlreadyRegisteredException.class);
    }
}