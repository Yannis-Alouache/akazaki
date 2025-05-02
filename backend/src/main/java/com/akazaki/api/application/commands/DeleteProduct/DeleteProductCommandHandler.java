package com.akazaki.api.application.commands.DeleteProduct;

import com.akazaki.api.domain.exceptions.ProductNotFoundException;
import com.akazaki.api.domain.model.Product;
import com.akazaki.api.domain.ports.in.commands.DeleteProductCommand;
import com.akazaki.api.domain.ports.out.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.File;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DeleteProductCommandHandler {
    private static final Logger logger = LoggerFactory.getLogger(DeleteProductCommandHandler.class);
    private final ProductRepository productRepository;

    @Value("${upload.dir}")
    private String uploadDir;

    public void handle(DeleteProductCommand command) {
        Optional<Product> optionalProduct = productRepository.findById(command.productId());
        if (optionalProduct.isEmpty()) {
            throw new ProductNotFoundException();
        }

        Product product = optionalProduct.get();
        String imageUrl = product.getImageUrl();

        try {
            productRepository.deleteById(command.productId());
            logger.info("Produit avec l'ID {} supprimé de la base de données.", command.productId());
        } catch (Exception e) {
            throw new IllegalStateException("Erreur lors de la suppression du produit", e);
        }

        if (imageUrl != null) {
            File imageFile = new File(uploadDir, imageUrl.replace("/uploads/", ""));
            if (imageFile.exists() && !imageFile.delete()) {
                throw new IllegalStateException("Échec de la suppression de l'image " + imageUrl);
            }
        }
    }
}
