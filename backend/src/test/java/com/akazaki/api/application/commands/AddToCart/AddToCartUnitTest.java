package com.akazaki.api.application.commands.AddToCart;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.logging.Logger;

import com.akazaki.api.config.fixtures.CategoryFixture;
import com.akazaki.api.infrastructure.persistence.Category.InMemoryCategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.akazaki.api.config.fixtures.ProductFixture;
import com.akazaki.api.config.fixtures.UserFixture;
import com.akazaki.api.domain.model.Cart;
import com.akazaki.api.domain.model.CartItem;
import com.akazaki.api.domain.ports.in.commands.AddToCartCommand;
import com.akazaki.api.domain.ports.out.CartRepository;
import com.akazaki.api.domain.ports.out.ProductRepository;
import com.akazaki.api.domain.ports.out.UserRepository;
import com.akazaki.api.infrastructure.persistence.Cart.InMemoryCartRepository;
import com.akazaki.api.infrastructure.persistence.Product.InMemoryProductRepository;
import com.akazaki.api.infrastructure.persistence.User.InMemoryUserRepository;

@SpringBootTest
@ActiveProfiles("test")
public class AddToCartUnitTest {
    private CartRepository cartRepository;
    private AddToCartCommandHandler handler;
    private UserFixture userFixture;
    private ProductFixture productFixture;

    @BeforeEach
    void setUp() {
        cartRepository = new InMemoryCartRepository();
        ProductRepository productRepository = new InMemoryProductRepository();
        UserRepository userRepository = new InMemoryUserRepository();
        handler = new AddToCartCommandHandler(cartRepository, productRepository, userRepository);

        userFixture = new UserFixture(userRepository);
        productFixture = new ProductFixture(productRepository, new CategoryFixture(new InMemoryCategoryRepository()));

        userFixture.saveUsers();
        productFixture.init(); // Explicitly call init to initialize the 'drink' product
        productFixture.saveProducts();
    }

    @Test
    public void addAProductToCartSuccesfully() {
        // given
        AddToCartCommand command = new AddToCartCommand(
            userFixture.adminUser.getId(),
            productFixture.drink.getId()
        );

        CartItem expectedCartItem = CartItem.builder()
                .id(1L)
                .product(productFixture.drink)
                .quantity(1)
                .build();
        
        Cart expectedCart = Cart.builder()
                .id(1L)
                .user(userFixture.adminUser)
                .cartItems(List.of(expectedCartItem))
                .build();

        // when
        handler.handle(command);
        Cart cart = cartRepository.findByUserId(userFixture.adminUser.getId()).orElseThrow();
        Logger.getGlobal().info(cart.toString());

        // then
        assertThat(cart).isEqualTo(expectedCart);
    }

}
