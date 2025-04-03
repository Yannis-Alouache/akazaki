package com.akazaki.api.infrastructure.persistence.Category;

import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaCategoryRepository extends CrudRepository<CategoryEntity, Long> {
    boolean existsByName(String name);

    @NonNull
    @Override
    List<CategoryEntity> findAll();
}