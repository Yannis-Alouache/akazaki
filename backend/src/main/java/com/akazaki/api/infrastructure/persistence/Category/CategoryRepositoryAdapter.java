package com.akazaki.api.infrastructure.persistence.Category;

import com.akazaki.api.domain.model.Category;
import com.akazaki.api.domain.ports.out.CategoryRepository;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("prod")
@RequiredArgsConstructor
public class CategoryRepositoryAdapter implements CategoryRepository {
    private final JpaCategoryRepository repository;
    private final CategoryMapper mapper;

    @Override
    public Category save(Category category) {
        CategoryEntity entity = mapper.toEntity(category);
        CategoryEntity savedEntity = repository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public boolean existsByName(String name) {
        return repository.existsByName(name);
    }
    
    @Override
    public List<Category> findAll() {
        return mapper.toDomainList(repository.findAll());
    }

    @Override
    public List<Category> findAllById(List<Long> categoryIds) {
        return mapper.toDomainList(repository.findAllById(categoryIds));
    }
}