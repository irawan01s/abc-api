package com.abc.api.services;

import com.abc.api.entities.Category;
import com.abc.api.exceptions.AlreadyExistsException;
import com.abc.api.exceptions.ResourceNotFoundException;
import com.abc.api.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    public Category getById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
    }

    public Category create(Category request) {
        return Optional.of(request).filter(c -> !categoryRepository.existsByName(c.getName()))
                .map(categoryRepository::save)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, request.getName() + " already exists"));
    }

    public Category update(Category request, Long id) {
        return Optional.ofNullable(getById(id)).map(category -> {
            category.setName(request.getName());
            category.setDescription(request.getDescription());
            return categoryRepository.save(category);
        }).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
    }

    public void delete(Long id) {
        categoryRepository.findById(id).ifPresentOrElse(categoryRepository::delete, () -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        });
    }
}
