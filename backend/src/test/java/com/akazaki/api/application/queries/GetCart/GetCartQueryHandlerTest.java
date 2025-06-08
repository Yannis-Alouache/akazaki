package com.akazaki.api.application.queries.GetCart;

import com.akazaki.api.config.fixtures.CartFixture;
import com.akazaki.api.config.fixtures.UserFixture;
import com.akazaki.api.domain.exceptions.UserNotFoundException;
import com.akazaki.api.domain.model.Cart;
import com.akazaki.api.domain.ports.in.queries.GetCartQuery;
import com.akazaki.api.domain.ports.out.CartRepository;
import com.akazaki.api.domain.ports.out.UserRepository;
import com.akazaki.api.infrastructure.persistence.Cart.InMemoryCartRepository;
import com.akazaki.api.infrastructure.persistence.User.InMemoryUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Get Cart Unit Tests")
class GetCartQueryHandlerTest {
    private GetCartQueryHandler getCartQueryHandler;
    private CartRepository cartRepository;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        cartRepository = new InMemoryCartRepository();
        userRepository = new InMemoryUserRepository();
        getCartQueryHandler = new GetCartQueryHandler(cartRepository, userRepository);
    }

    @Test
    @DisplayName("Should return existing cart when user has one")
    void shouldReturnExistingCartWhenUserHasOne() {
        // Given
        userRepository.save(UserFixture.adminUser);
        cartRepository.save(CartFixture.adminUserCartWithDrinkItem);
        GetCartQuery query = new GetCartQuery(UserFixture.adminUser.getId());

        // When
        Cart result = getCartQueryHandler.handle(query);

        // Then
        assertThat(result).isEqualTo(CartFixture.adminUserCartWithDrinkItem);
    }

    @Test
    @DisplayName("Should create empty cart when user has none")
    void shouldCreateEmptyCartWhenUserHasNone() {
        // Given
        userRepository.save(UserFixture.adminUser);
        GetCartQuery query = new GetCartQuery(UserFixture.adminUser.getId());

        // When
        Cart result = getCartQueryHandler.handle(query);

        // Then
        assertThat(result.getUser()).isEqualTo(UserFixture.adminUser);
        assertThat(result.getCartItems()).isEmpty();
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when user does not exist")
    void shouldThrowUserNotFoundExceptionWhenUserDoesNotExist() {
        // Given
        GetCartQuery query = new GetCartQuery(999L);

        // When/Then
        assertThatThrownBy(() -> getCartQueryHandler.handle(query))
            .isInstanceOf(UserNotFoundException.class);
    }
} 