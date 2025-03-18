package com.akazaki.api.application.commands.StoreImage;

import com.akazaki.api.domain.model.Product;
import com.akazaki.api.domain.ports.in.commands.StoreImageCommand;
import com.akazaki.api.domain.ports.out.ImageRepository;
import com.akazaki.api.domain.ports.out.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class StoreImageCommandHandler {
    private final ImageRepository imageRepository;
    private final ProductRepository productRepository;

    public String handle(StoreImageCommand command) {
        Product product = productRepository.findById(command.productId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        String path = imageRepository.save(
                command.fileName(),
                command.contentType(),
                command.imageStream()
        );

        product.setImageUrl(path);
        productRepository.save(product);
        return path;
    }
}
