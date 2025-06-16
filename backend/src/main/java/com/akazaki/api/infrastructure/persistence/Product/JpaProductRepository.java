package com.akazaki.api.infrastructure.persistence.Product;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface JpaProductRepository extends JpaRepository<ProductEntity, Long> {
    boolean existsByName(String name);

    @Query("SELECT p FROM ProductEntity p JOIN FETCH p.categories WHERE p.id = :id")
    Optional<ProductEntity> findByIdWithCategories(@Param("id") Long id);
    
    void deleteById(@Param("id") Long id);

    @NonNull
    @Override
    List<ProductEntity> findAll();

    @Query("SELECT DISTINCT p FROM ProductEntity p JOIN p.categories c WHERE c.name IN :categoryNames")
    Page<ProductEntity> findByCategoryNames(@Param("categoryNames") List<String> categoryNames, Pageable pageable);
}