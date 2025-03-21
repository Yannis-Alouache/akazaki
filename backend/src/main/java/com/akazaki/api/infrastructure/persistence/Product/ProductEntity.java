package com.akazaki.api.infrastructure.persistence.Product;

import com.akazaki.api.infrastructure.persistence.Category.CategoryEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private int stock;

    @Column()
    private String imageUrl;

    @ManyToMany
    private List<CategoryEntity> categories;
    
    // @OneToMany
    // private List<ReviewEntity> reviews;
    
    // @ManyToOne
    // private PromotionEntity promotions;
}
