package com.akazaki.api.application.commands.UpdateProduct;

import com.akazaki.api.domain.exceptions.ProductAlreadyExistException;
import com.akazaki.api.domain.exceptions.ProductNotFoundException;
import com.akazaki.api.domain.exceptions.UnableToFetchCategoriesException;
import com.akazaki.api.domain.model.Category;
import com.akazaki.api.domain.model.Product;
import com.akazaki.api.domain.ports.in.commands.UpdateProductCommand;
import com.akazaki.api.domain.ports.out.CategoryRepository;
import com.akazaki.api.domain.ports.out.ProductRepository;
import com.akazaki.api.infrastructure.exceptions.UnableToDeleteFileException;
import com.akazaki.api.infrastructure.exceptions.UnableToSaveFileException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class UpdateProductCommandHandler {
    private static final Logger log = LoggerFactory.getLogger(UpdateProductCommandHandler.class);
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Value("${upload.dir}")
    private String uploadDir;

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produit introuvable avec l'ID : " + id));
    }

    public void handle(UpdateProductCommand command) {
        List<Category> categories = categoryRepository.findAllById(command.categoryIds());
        Product product = productRepository.findById(command.productId()).orElseThrow(
                ProductNotFoundException::new
        );

        if (categories.size() != command.categoryIds().size()) {
            throw new UnableToFetchCategoriesException();
        }

        if (productRepository.existsByName(command.name()) && !product.getName().equals(command.name())) {
            throw new ProductAlreadyExistException();
        }

        // Gestion de l'image
        if (command.imageFile() != null && !command.imageFile().isEmpty()) {
            String oldImageUrl = product.getImageUrl();
            if (oldImageUrl != null && !oldImageUrl.isEmpty()) {
                File oldImageFile = new File(uploadDir, oldImageUrl);
                if (oldImageFile.exists() && !oldImageFile.delete()) {
                    log.error("Failed to delete old image file: {}", oldImageFile.getAbsolutePath());
                    throw new UnableToDeleteFileException();
                }
            }

            try {
                Path targetPath = Path.of(uploadDir, Objects.requireNonNull(command.imageFile().getOriginalFilename()));
                Files.copy(command.imageFile().getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
                product.setImageUrl(targetPath.getFileName().toString());
            } catch (IOException e) {
                log.error("Failed to save new image file: {}", e.getMessage());
                throw new UnableToSaveFileException();
            }
        }

        product.setName(command.name());
        product.setDescription(command.description());
        product.setPrice(command.price());
        product.setStock(command.stock());
        product.setCategories(categories);

        productRepository.save(product);
    }
}