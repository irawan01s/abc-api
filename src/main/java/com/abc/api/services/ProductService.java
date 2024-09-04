package com.abc.api.services;

import com.abc.api.entities.Category;
import com.abc.api.entities.Product;
import com.abc.api.exceptions.ProductNotFoundException;
import com.abc.api.payload.request.product.ProductCreateRequest;
import com.abc.api.payload.request.product.ProductUpdateRequest;
import com.abc.api.payload.response.product.ProductResponse;
import com.abc.api.repositories.CategoryRepository;
import com.abc.api.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public ResponseEntity<Product> getById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        return ResponseEntity.ok(product);
    }

    public List<Product> getByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }


    public ProductResponse create(ProductCreateRequest request) {
        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(()-> {
                    Category newCategory = new Category();
                    newCategory.setName(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });

        Product product = new Product();
        product.setTitle(request.getTitle());
        product.setSubtitle(request.getSubtitle());
        product.setPrice(request.getPrice());
        product.setUnit(request.getUnit());
        product.setDescription(request.getDescription());
        product.setLocation(request.getDescription());
        product.setNotes(request.getDescription());
        product.setCategory(category);

        productRepository.save(product);

        return toProductResponse(product);
    }

    public ProductResponse update(Long id, ProductUpdateRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        product.setTitle(request.getTitle());
        product.setSubtitle(request.getSubtitle());
        product.setPrice(request.getPrice());
        product.setUnit(request.getUnit());
        product.setDescription(request.getDescription());
        product.setLocation(request.getDescription());
        product.setNotes(request.getDescription());

        Category category = categoryRepository.findByName(request.getCategory().getName());
        product.setCategory(category);

        productRepository.save(product);

        return toProductResponse(product);
    }

    public void delete(Long id) {
        productRepository.findById(id).ifPresentOrElse(productRepository::delete, () -> {
            throw new ProductNotFoundException("Product not found");
        });
    }

    private ProductResponse toProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .title(product.getTitle())
                .subtitle(product.getSubtitle())
                .price(product.getPrice())
                .unit(product.getUnit())
                .description(product.getDescription())
                .location(product.getLocation())
                .notes(product.getNotes())
                .category(product.getCategory())
                .build();
    }
}
