package com.abc.api.repositories;

import com.abc.api.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
//    @Override
//    Optional<Category> findById(Long id);

    Category findByName(String name);

    boolean existsByName(String name);
}
