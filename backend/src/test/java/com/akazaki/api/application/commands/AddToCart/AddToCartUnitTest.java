package com.akazaki.api.application.commands.AddToCart;

import static org.assertj.core.api.Assertions.assertThat;


import com.akazaki.api.config.fixtures.CartFixture;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import com.akazaki.api.config.fixtures.ProductFixture;
import com.akazaki.api.config.fixtures.UserFixture;
import com.akazaki.api.domain.model.Cart;
import com.akazaki.api.domain.ports.in.commands.AddToCartCommand;
import com.akazaki.api.domain.ports.out.CartRepository;
import com.akazaki.api.domain.ports.out.ProductRepository;
import com.akazaki.api.domain.ports.out.UserRepository;
import com.akazaki.api.infrastructure.persistence.Cart.InMemoryCartRepository;
import com.akazaki.api.infrastructure.persistence.Product.InMemoryProductRepository;
import com.akazaki.api.infrastructure.persistence.User.InMemoryUserRepository;

@Tag("unit")
@DisplayName("Add to cart Unit Tests")
public class AddToCartUnitTest {
    private CartRepository cartRepository;
    private AddToCartCommandHandler handler;


    @BeforeEach
    void setUp() {
        cartRepository = new InMemoryCartRepository();
        ProductRepository productRepository = new InMemoryProductRepository();
        UserRepository userRepository = new InMemoryUserRepository();
        handler = new AddToCartCommandHandler(cartRepository, productRepository, userRepository);

        userRepository.save(UserFixture.adminUser);
        productRepository.save(ProductFixture.ramuneFraise);
    }

    @Test
    @DisplayName("Add A Product To Cart Successfully")
    public void addAProductToCartSuccesfully() {
        // given
        AddToCartCommand command = new AddToCartCommand(
            UserFixture.adminUser.getId(),
            ProductFixture.ramuneFraise.getId()
        );

        // when
        handler.handle(command);
        Cart cart = cartRepository.findByUserId(UserFixture.adminUser.getId()).orElseThrow();

        // then
        assertThat(cart)
                .usingRecursiveComparison()
                .ignoringFields("id", "user.id", "cartItems.id", "cartItems.product.id", "cartItems.product.categories.id")
                .isEqualTo(CartFixture.adminUserCartWithDrinkItem);
    }

}
