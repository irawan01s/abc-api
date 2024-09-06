package com.abc.api.services;

import com.abc.api.entities.Category;
import com.abc.api.entities.Product;
import com.abc.api.exceptions.ResourceNotFoundException;
import com.abc.api.payload.request.products.ProductCreateRequest;
import com.abc.api.payload.request.products.ProductSearchRequest;
import com.abc.api.payload.request.products.ProductUpdateRequest;
import com.abc.api.payload.response.categories.CategoryResponse;
import com.abc.api.payload.response.products.ProductResponse;
import com.abc.api.repositories.CategoryRepository;
import com.abc.api.repositories.ProductRepository;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    public Page<ProductResponse> getAll(ProductSearchRequest request) {
        Specification<Product> specification = (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (Objects.nonNull(request.getTitle())) {
                predicates.add(builder.or(
                        builder.like(root.get("title"),"%" + request.getTitle() + "%"),
                        builder.like(root.get("subtitle"),"%" + request.getSubtitle() + "%")
                ));
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<Product> products = productRepository.findAll(specification, pageable);

        List<ProductResponse> productResponses = products.getContent().stream()
                .map(this::toProductResponse)
                .toList();
        return new PageImpl<>(productResponses, pageable, products.getTotalElements());
    }

    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    public List<Product> getByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Transactional
    public Product create(ProductCreateRequest request) {
        Category category = categoryRepository.findById(request.getCategory().getId())
                .orElseGet(() -> {
                    Category newCategory = new Category();
                    newCategory.setName(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });

        Product product = new Product();
        product.setTitle(request.getTitle());
        product.setSubtitle(request.getSubtitle());
        product.setPriceMin(request.getPriceMin());
        product.setPriceMax(request.getPriceMax());
        product.setUnit(request.getUnit());
        product.setDescription(request.getDescription());
        product.setLocation(request.getLocation());
        product.setNotes(request.getNotes());
        product.setCategory(category);

        return productRepository.save(product);
//        return toProductResponse(products);
    }

    public Product update(Long id, ProductUpdateRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        product.setTitle(request.getTitle());
        product.setSubtitle(request.getSubtitle());
        product.setPriceMin(request.getPriceMin());
        product.setPriceMax(request.getPriceMax());
        product.setUnit(request.getUnit());
        product.setDescription(request.getDescription());
        product.setLocation(request.getLocation());
        product.setNotes(request.getNotes());

        Category category = categoryRepository.findByName(request.getCategory().getName());
        product.setCategory(category);

        return productRepository.save(product);

//        return toProductResponse(products);
    }

    public void delete(Long id) {
        productRepository.findById(id).ifPresentOrElse(productRepository::delete, () -> {
            throw new ResourceNotFoundException("Product not found");
        });
    }

    private ProductResponse toProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .title(product.getTitle())
                .subtitle(product.getSubtitle())
                .priceMin(product.getPriceMin())
                .priceMax(product.getPriceMax())
                .unit(product.getUnit())
                .description(product.getDescription())
                .location(product.getLocation())
                .notes(product.getNotes())
                .category(CategoryResponse.builder()
                        .id(product.getCategory().getId())
                        .name(product.getCategory().getName())
                        .description(product.getCategory().getDescription())
                        .build())
                .build();
    }
}
