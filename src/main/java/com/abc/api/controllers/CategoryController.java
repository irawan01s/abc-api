package com.abc.api.controllers;

import com.abc.api.entities.Category;
import com.abc.api.exceptions.AlreadyExistsException;
import com.abc.api.exceptions.ResourceNotFoundException;
import com.abc.api.payload.response.WebResponse;
import com.abc.api.payload.response.categories.CategoryResponse;
import com.abc.api.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<WebResponse> getAll() {
        try {
            List<Category> categories = categoryService.getAll();
            return ResponseEntity.ok(WebResponse.builder()
                            .status(true)
                            .message("Success")
                    .data(categories).build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(WebResponse.builder()
                            .status(false)
                            .message(e.getMessage())
                            .build());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<WebResponse> getCategoryById(@PathVariable Long id) {
        try {
            Category category = categoryService.getById(id);
            return ResponseEntity.ok(WebResponse.builder()
                            .status(true)
                            .message("Success")
                            .data(toCategoryResponse(category))
                            .build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(WebResponse.builder()
                            .status(false)
                            .message(e.getMessage())
                            .build());
        }
    }

    @PostMapping
    public ResponseEntity<WebResponse> createCategory(@RequestBody Category request) {
        try {
            Category category = categoryService.create(request);
            return ResponseEntity.ok(WebResponse.builder()
                    .status(true)
                    .message("Success")
                    .data(toCategoryResponse(category))
                    .build());
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(WebResponse.builder()
                            .status(false)
                            .message(e.getMessage())
                            .build());
        }
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<WebResponse> updateCategory(@PathVariable Long id, @RequestBody Category request) {
        try {
            categoryService.update(request, id);
            return ResponseEntity.ok(WebResponse.builder()
                    .status(true)
                    .message("Success")
                    .build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(WebResponse.builder()
                            .status(false)
                            .message(e.getMessage())
                            .build());
        }
    }

    @DeleteMapping(path = "id")
    public ResponseEntity<WebResponse> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.delete(id);
            return ResponseEntity.ok(WebResponse.builder()
                    .status(true)
                    .message("Success")
                    .build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(WebResponse.builder()
                            .status(false)
                            .message(e.getMessage())
                            .build());
        }
    }

    private CategoryResponse toCategoryResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }
}
