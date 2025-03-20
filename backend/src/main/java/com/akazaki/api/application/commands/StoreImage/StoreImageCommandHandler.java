package com.akazaki.api.application.commands.StoreImage;

import com.akazaki.api.domain.exceptions.ProductNotFoundException;
import com.akazaki.api.domain.model.Image;
import com.akazaki.api.domain.model.Product;
import com.akazaki.api.domain.ports.in.commands.StoreImageCommand;
import com.akazaki.api.domain.ports.out.ImageRepository;
import com.akazaki.api.domain.ports.out.ProductRepository;
import com.akazaki.api.infrastructure.exceptions.InvalidFileTypeException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class StoreImageCommandHandler {
    private final ImageRepository imageRepository;
    private final ProductRepository productRepository;

    public String handle(StoreImageCommand command) {
        String contentType = command.contentType().toLowerCase();
        if (!contentType.startsWith("image/"))
            throw new InvalidFileTypeException();

        Product product = productRepository.findById(command.productId())
                .orElseThrow(ProductNotFoundException::new);

        String path = imageRepository.save(
            new Image(command.fileName(), command.imageStream())
        );

        product.setImageUrl(path);
        productRepository.save(product);
        return path;
    }
}
