package com.akazaki.api.application.commands.DeleteProduct;

import com.akazaki.api.domain.exceptions.ProductNotFoundException;
import com.akazaki.api.domain.model.Product;
import com.akazaki.api.domain.ports.in.commands.DeleteProductCommand;
import com.akazaki.api.domain.ports.out.ProductRepository;
import com.akazaki.api.infrastructure.exceptions.UnableToDeleteFileException;
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
    private final ProductRepository productRepository;

    @Value("${upload.dir}")
    private String uploadDir;

    public void handle(DeleteProductCommand command) {
        Product product = productRepository.findById(command.productId()).orElseThrow(
            ProductNotFoundException::new
        );

        String imageUrl = product.getImageUrl();

        if (imageUrl != null) {
            File imageFile = new File(uploadDir, imageUrl.replace("/uploads/", ""));
            if (imageFile.exists() && !imageFile.delete()) {
                throw new UnableToDeleteFileException();
            }
        }

        productRepository.deleteById(command.productId());
    }
}
