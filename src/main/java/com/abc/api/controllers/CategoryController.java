package com.abc.api.controllers;

import com.abc.api.entities.Category;
import com.abc.api.entities.User;
import com.abc.api.payload.response.WebResponse;
import com.abc.api.payload.response.categories.CategoryResponse;
import com.abc.api.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<WebResponse<Object>> getAll() {

            List<Category> categories = categoryService.getAll();
            return ResponseEntity.ok(WebResponse.builder()
                    .status(true).data(categories).build());

    }

    @GetMapping("/{id}")
    public WebResponse<CategoryResponse> getCategoryById(@PathVariable Long id) {
            Category category = categoryService.getById(id);
            return WebResponse.<CategoryResponse>builder()
                            .status(true)
                            .data(toCategoryResponse(category))
                            .build();
    }

    @PostMapping
    public WebResponse<CategoryResponse> createCategory(@AuthenticationPrincipal User user, @RequestBody Category request) {
            Category category = categoryService.create(user, request);
            return WebResponse.<CategoryResponse>builder()
                    .status(true)
                    .data(toCategoryResponse(category))
                    .build();

    }

    @PutMapping(path = "/{id}")
    public WebResponse<?> updateCategory(@AuthenticationPrincipal User user, @PathVariable Long id, @RequestBody Category request) {
            categoryService.update(user, id, request);
            return WebResponse.builder()
                    .status(true)
                    .build();
    }

    @DeleteMapping(path = "id")
    public WebResponse<?> deleteCategory(@PathVariable Long id) {
            categoryService.delete(id);
            return WebResponse.builder()
                    .status(true)
                    .build();
    }

    private CategoryResponse toCategoryResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }
}
