package com.akazaki.api.application.queries.ListProducts;

import com.akazaki.api.domain.model.Product;
import com.akazaki.api.domain.ports.in.queries.ListProductsQuery;
import com.akazaki.api.domain.ports.out.ProductRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ListProductsQueryHandler {
    private final ProductRepository productRepository;

    public ListProductsQueryHandler(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> handle(ListProductsQuery query) {
        return productRepository.findAll();
    }
} 