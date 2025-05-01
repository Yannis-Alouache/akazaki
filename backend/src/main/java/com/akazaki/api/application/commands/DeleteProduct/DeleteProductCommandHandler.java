package com.akazaki.api.application.commands.DeleteProduct;

import com.akazaki.api.domain.model.Product;
import com.akazaki.api.domain.ports.in.commands.DeleteProductCommand;
import com.akazaki.api.domain.ports.out.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DeleteProductCommandHandler {
    private static final Logger logger = LoggerFactory.getLogger(DeleteProductCommandHandler.class);
    private final ProductRepository productRepository;

    @Value("${upload.dir}")
    private String uploadDir;

    @Transactional
    public void handle(DeleteProductCommand command) {
        Optional<Product> optionalProduct = productRepository.findById(command.productId());
        if (optionalProduct.isEmpty()) {
            logger.warn("Produit avec l'ID {} introuvable.", command.productId());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Produit avec l'ID " + command.productId() + " introuvable."
            );
        }

        Product product = optionalProduct.get();
        String imageUrl = product.getImageUrl();

        // Suppression du produit (base de données)
        try {
            productRepository.deleteById(command.productId());
            logger.info("Produit avec l'ID {} supprimé de la base de données.", command.productId());
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression du produit avec l'ID {}: {}", command.productId(), e.getMessage());
            throw new IllegalStateException("Erreur lors de la suppression du produit", e);
        }

        // Suppression de l'image (fichier)
        if (imageUrl != null) {
            File imageFile = new File(uploadDir, imageUrl.replace("/uploads/", ""));
            if (imageFile.exists()) {
                if (imageFile.delete()) {
                    logger.info("Image {} supprimée avec succès.", imageUrl);
                } else {
                    logger.error("Échec de la suppression de l'image {}.", imageUrl);
                    throw new IllegalStateException("Échec de la suppression de l'image " + imageUrl);
                }
            } else {
                logger.warn("Image {} introuvable dans le dossier d'upload.", imageUrl);
            }
        }
    }
}
