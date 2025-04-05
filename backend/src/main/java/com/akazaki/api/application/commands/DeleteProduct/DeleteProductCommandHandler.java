package com.akazaki.api.application.commands.DeleteProduct;

import com.akazaki.api.domain.exceptions.ProductNotFoundException;
import com.akazaki.api.domain.ports.in.commands.DeleteProductCommand;
import com.akazaki.api.domain.ports.out.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteProductCommandHandler {
    private final ProductRepository productRepository;

    public void handle(DeleteProductCommand command) {
        if (productRepository.findById(command.productId()).isEmpty()) {
            throw new ProductNotFoundException();
        }
        productRepository.deleteById(command.productId());
    }
}
