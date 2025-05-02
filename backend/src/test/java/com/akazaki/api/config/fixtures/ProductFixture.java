package com.akazaki.api.config.fixtures;

import com.akazaki.api.domain.model.Product;
import com.akazaki.api.domain.ports.out.ProductRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;


@Component
public class ProductFixture {

    private final ProductRepository productRepository;
    public Product drink;

    public ProductFixture(ProductRepository productRepository, CategoryFixture categoryFixture) {
        this.productRepository = productRepository;
        
        categoryFixture.saveCategories();
        this.drink = Product.create("Ramune Fraise", "the product description", 3.99, 30, "/uploads/image.png", List.of(categoryFixture.japan));
    }

    public void saveProducts() {
       this.drink = productRepository.save(drink);
    }
}
