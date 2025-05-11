package com.akazaki.api.config.fixtures;

import com.akazaki.api.domain.model.Product;
import org.springframework.stereotype.Component;
import java.util.List;


@Component
public class ProductFixture {
    public static final Product drink = Product.create("Ramune Fraise", "the product description", 3.99, 30, "/uploads/image.png", List.of(CategoryFixture.japan));
}
