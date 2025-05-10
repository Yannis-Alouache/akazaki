package com.akazaki.api.config.fixtures;

import com.akazaki.api.domain.model.Category;
import com.akazaki.api.domain.ports.out.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class CategoryFixture {
    public final static Category drink = new Category("Drink");
    public final static Category japan = new Category("Japan");
}
