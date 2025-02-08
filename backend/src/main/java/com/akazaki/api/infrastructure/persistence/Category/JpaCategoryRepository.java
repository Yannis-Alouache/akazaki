package com.akazaki.api.infrastructure.persistence.Category;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaCategoryRepository extends CrudRepository<CategoryEntity, Long> {
    boolean existsByName(String name);
}