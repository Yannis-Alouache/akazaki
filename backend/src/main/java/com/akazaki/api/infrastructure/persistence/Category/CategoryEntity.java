package com.akazaki.api.infrastructure.persistence.Category;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.akazaki.api.domain.model.Category;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    public static CategoryEntity fromDomain(Category category) {
        return new CategoryEntity(
            category.getId(),
            category.getName()
        );
    }

    public Category toDomain() {
        return new Category(id, name);
    }
}