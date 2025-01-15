package com.akazaki.api.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;
    private BigDecimal price;
    private int stock;
    private String imageUrl;

    @ManyToMany
    private List<Category> categories;

    @OneToMany
    private List<Review> reviews;

    @ManyToMany
    @JoinTable(name = "ProductPromotion")
    private List<Promotion> promotions;


}
