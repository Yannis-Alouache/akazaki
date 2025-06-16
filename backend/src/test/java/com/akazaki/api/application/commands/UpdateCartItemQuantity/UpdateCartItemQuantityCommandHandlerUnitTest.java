package com.akazaki.api.application.commands.UpdateCartItemQuantity;

import static org.junit.Assert.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import com.akazaki.api.application.commands.UpdateCartItemQuantityCommandHandler;
import com.akazaki.api.config.fixtures.CartFixture;
import com.akazaki.api.config.fixtures.ProductFixture;
import com.akazaki.api.config.fixtures.UserFixture;
import com.akazaki.api.domain.exceptions.InsufficientStockException;
import com.akazaki.api.domain.exceptions.ProductNotFoundException;
import com.akazaki.api.domain.model.Cart;
import com.akazaki.api.domain.ports.in.commands.UpdateCartItemQuantityCommand;
import com.akazaki.api.domain.ports.out.CartRepository;
import com.akazaki.api.domain.ports.out.ProductRepository;
import com.akazaki.api.domain.ports.out.UserRepository;
import com.akazaki.api.infrastructure.persistence.Cart.InMemoryCartRepository;
import com.akazaki.api.infrastructure.persistence.Product.InMemoryProductRepository;
import com.akazaki.api.infrastructure.persistence.User.InMemoryUserRepository;

@Tag("unit")
@DisplayName("Update cart item quantity Unit Tests")
public class UpdateCartItemQuantityCommandHandlerUnitTest {
    private UpdateCartItemQuantityCommandHandler handler;
    private CartRepository cartRepository;
    private ProductRepository productRepository;
    private UserRepository userRepository;

    private final int QUANTITY = 2;

    @BeforeEach
    void setUp() {
        cartRepository = new InMemoryCartRepository();
        productRepository = new InMemoryProductRepository();
        userRepository = new InMemoryUserRepository();
        handler = new UpdateCartItemQuantityCommandHandler(cartRepository, productRepository);

        userRepository.save(UserFixture.adminUser);
        productRepository.save(ProductFixture.ramuneFraise);
        cartRepository.save(CartFixture.adminUserCartWithDrinkItem);
    }

    @Test
    @DisplayName("Update cart item quantity successfully")
    void updateCartItemQuantitySuccessfully() {
        // given
        UpdateCartItemQuantityCommand command = new UpdateCartItemQuantityCommand(
            UserFixture.adminUser.getId(),
            ProductFixture.ramuneFraise.getId(),
            QUANTITY
        );

        handler.handle(command);
        
        Cart cart = cartRepository.findByUserId(UserFixture.adminUser.getId()).orElseThrow();

        assertThat(cart.getCartItems().get(0).getQuantity()).isEqualTo(QUANTITY);
    }

    @Test
    @DisplayName("should throw exception when quantity exceeds stock")
    void shouldThrowExceptionWhenQuantityExceedsStock() {
        // given
        UpdateCartItemQuantityCommand command = new UpdateCartItemQuantityCommand(
            UserFixture.adminUser.getId(),
            ProductFixture.ramuneFraise.getId(),
            ProductFixture.ramuneFraise.getStock() + 1
        );

        assertThrows(InsufficientStockException.class, () -> handler.handle(command));
    }

    @Test
    @DisplayName("should throw exception when product does not exist")
    void shouldThrowExceptionWhenProductDoesNotExist() {
        // given
        UpdateCartItemQuantityCommand command = new UpdateCartItemQuantityCommand(
            UserFixture.adminUser.getId(),
            9999L,
            QUANTITY
        );

        assertThrows(ProductNotFoundException.class, () -> handler.handle(command));
    }

    @Test
    @DisplayName("should remove item from cart when quantity is 0")
    void shouldRemoveItemFromCartWhenQuantityIs0() {
        final int PRODUCT_IN_CART = CartFixture.adminUserCartWithDrinkItem.getCartItems().size();

        // given
        UpdateCartItemQuantityCommand command = new UpdateCartItemQuantityCommand(
            UserFixture.adminUser.getId(),
            ProductFixture.ramuneFraise.getId(),
            0
        );

        handler.handle(command);

        Cart cart = cartRepository.findByUserId(UserFixture.adminUser.getId()).orElseThrow();
        
        assertThat(cart.getCartItems()).hasSize(PRODUCT_IN_CART - 1);
    }
}