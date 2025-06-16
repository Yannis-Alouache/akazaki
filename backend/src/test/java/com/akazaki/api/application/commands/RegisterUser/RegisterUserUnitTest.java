package com.akazaki.api.application.commands.RegisterUser;

import com.akazaki.api.application.commands.Register.RegisterUserCommandHandler;
import com.akazaki.api.config.fixtures.UserFixture;
import com.akazaki.api.domain.exceptions.EmailAlreadyRegisteredException;
import com.akazaki.api.domain.model.User;
import com.akazaki.api.domain.ports.in.commands.RegisterUserCommand;
import com.akazaki.api.infrastructure.persistence.User.InMemoryUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@Tag("unit")
@DisplayName("Register User Unit Tests")
@ExtendWith(MockitoExtension.class)
class RegisterUserUnitTest {

    private RegisterUserCommandHandler handler;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        InMemoryUserRepository repository = new InMemoryUserRepository();
        handler = new RegisterUserCommandHandler(repository, passwordEncoder);

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
    }

    @Test
    @DisplayName("Create A User Successfully")
    void createAUser() {
        UserFixture.basicUser.setId(1L); // to avoid null id in the comparison

        RegisterUserCommand command = new RegisterUserCommand(
                UserFixture.basicUser.getEmail(),
                "123456",
                UserFixture.basicUser.getFirstName(),
                UserFixture.basicUser.getLastName(),
                UserFixture.basicUser.getPhoneNumber(),
                UserFixture.basicUser.isAdmin()
        );

        User result = handler.handle(command);
        assertThat(result).usingRecursiveComparison().isEqualTo(UserFixture.basicUser);
    }

    @Test
    @DisplayName("Prevent Duplicate User")
    void preventDuplicateUser() {
        RegisterUserCommand command = new RegisterUserCommand(
                UserFixture.basicUser.getEmail(),
                "123456",
                UserFixture.basicUser.getFirstName(),
                UserFixture.basicUser.getLastName(),
                UserFixture.basicUser.getPhoneNumber(),
                UserFixture.basicUser.isAdmin()
        );

        // First creation should succeed
        handler.handle(command);

        // When/Then - Second creation should fail
        assertThatThrownBy(() -> handler.handle(command))
                .isInstanceOf(EmailAlreadyRegisteredException.class);
    }
}