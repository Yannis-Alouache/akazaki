package com.akazaki.api.application.commands;

import com.akazaki.api.domain.exceptions.CartNotFoundException;
import com.akazaki.api.domain.exceptions.InsufficientStockException;
import com.akazaki.api.domain.exceptions.ProductNotFoundException;
import com.akazaki.api.domain.model.Cart;
import com.akazaki.api.domain.model.Product;
import com.akazaki.api.domain.ports.in.commands.UpdateCartItemQuantityCommand;
import com.akazaki.api.domain.ports.out.CartRepository;
import com.akazaki.api.domain.ports.out.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class UpdateCartItemQuantityCommandHandler {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public UpdateCartItemQuantityCommandHandler(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    public Cart handle(UpdateCartItemQuantityCommand command) {
        // Vérifier que le produit existe
        Product product = productRepository.findById(command.productId())
                .orElseThrow(ProductNotFoundException::new);

        // Récupérer le panier de l'utilisateur
        Cart cart = cartRepository.findByUserId(command.userId())
                .orElseThrow(CartNotFoundException::new);

        Cart updatedCart = cart.copy();

        if (command.quantity() == 0) {
            updatedCart.removeItem(command.productId());
            return cartRepository.save(updatedCart);
        }

        if (command.quantity() > product.getStock())
            throw new InsufficientStockException();

       updatedCart.updateItemQuantity(command.productId(), command.quantity());

        return cartRepository.save(updatedCart);
    }
} 