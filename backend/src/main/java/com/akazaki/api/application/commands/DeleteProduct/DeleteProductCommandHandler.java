package com.akazaki.api.application.commands.DeleteProduct;

import com.akazaki.api.domain.exceptions.ProductNotFoundException;
import com.akazaki.api.domain.model.Product;
import com.akazaki.api.domain.ports.in.commands.DeleteProductCommand;
import com.akazaki.api.domain.ports.out.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DeleteProductCommandHandler {
    private static final Logger logger = LoggerFactory.getLogger(DeleteProductCommandHandler.class);
    private final ProductRepository productRepository;

    public void handle(DeleteProductCommand command) {

        Optional<Product> optionalProduct = productRepository.findById(command.productId());
        if (optionalProduct.isEmpty()) {
            logger.warn("Produit avec l'ID {} introuvable.", command.productId());
            return;
        }

        Product product = optionalProduct.get();

        String imageUrl = product.getImageUrl();
        if (imageUrl != null) {
            File imageFile = new File("uploads/" + imageUrl);
            if (imageFile.exists()) {
                boolean deleted = imageFile.delete();
                if (deleted) {
                    logger.info("Image {} supprimée avec succès.", imageUrl);
                } else {
                    logger.warn("Échec de la suppression de l'image {}.", imageUrl);
                }
            } else {
                logger.warn("Image {} introuvable dans le dossier d'upload.", imageUrl);
            }
        }

        productRepository.deleteById(command.productId());
        logger.info("Produit avec l'ID {} supprimé avec succès.", command.productId());
    }
}

