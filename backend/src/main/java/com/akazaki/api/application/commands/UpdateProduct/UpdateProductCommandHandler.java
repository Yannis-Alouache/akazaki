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
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UpdateProductCommandHandler {
    private static final Logger log = LoggerFactory.getLogger(UpdateProductCommandHandler.class);
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Value("${upload.dir}")
    private String uploadDir;

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

        if (command.imageFile() != null && !command.imageFile().isEmpty()) {
            String oldImageUrl = product.getImageUrl();
            if (oldImageUrl != null && !oldImageUrl.isBlank()) {
                String oldImageName = Path.of(oldImageUrl).getFileName().toString();
                File oldImageFile = new File(uploadDir, oldImageName);
                if (oldImageFile.exists() && !oldImageFile.delete()) {
                    throw new UnableToDeleteFileException();
                }
            }

            try {
                String originalFilename = Objects.requireNonNull(command.imageFile().getOriginalFilename());
                String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                String uniqueImageName = UUID.randomUUID() + extension;

                Path targetPath = Path.of(uploadDir, uniqueImageName);
                Files.copy(command.imageFile().getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

                product.setImageUrl("/uploads/" + uniqueImageName);
            } catch (IOException e) {
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