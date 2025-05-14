package com.akazaki.api.infrastructure.persistence.Product;

import lombok.NonNull;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaProductRepository extends CrudRepository<ProductEntity, Long> {
    boolean existsByName(String name);

    @Query("SELECT p FROM ProductEntity p JOIN FETCH p.categories WHERE p.id = :id")
    Optional<ProductEntity> findByIdWithCategories(@Param("id") Long id);
    
    void deleteById(@Param("id") Long id);

    @NonNull
    @Override
    List<ProductEntity> findAll();
}