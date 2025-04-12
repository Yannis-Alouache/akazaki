package com.akazaki.api.config.fixtures;

import com.akazaki.api.domain.model.Category;
import com.akazaki.api.domain.model.Product;
import com.akazaki.api.domain.ports.out.ProductRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;


@Component
public class ProductFixture {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryFixture categoryFixture;

    public Product drink;

    @PostConstruct
    public void init() {
        categoryFixture.saveCategories();
        this.drink = Product.create("Ramune Fraise", "the product description", 3.99, 30, "/uploads/image.png", List.of(categoryFixture.category));
    }

    public void saveProducts() {
        drink = productRepository.save(drink);
    }
}
