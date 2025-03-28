package com.akazaki.api.application.queries.GetProduct;

import com.akazaki.api.domain.exceptions.ProductNotFoundException;
import com.akazaki.api.domain.model.Product;
import com.akazaki.api.domain.ports.in.queries.GetProductQuery;
import com.akazaki.api.domain.ports.out.ProductRepository;

import org.springframework.stereotype.Component;

@Component
public class GetProductQueryHandler {
    private final ProductRepository productRepository;
    
    public GetProductQueryHandler(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product handle(GetProductQuery query) {
        return productRepository.findById(query.id())
                .orElseThrow(() -> new ProductNotFoundException());
    }
}