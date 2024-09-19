package com.abc.api.payload.response.products;

import com.abc.api.entities.Category;
import com.abc.api.entities.Feature;
import com.abc.api.payload.response.categories.CategoryResponse;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {
    private Long id;

    private String title;

    private String subtitle;

    private BigDecimal priceMin;

    private BigDecimal priceMax;

    private String unit;

    private String description;

    private String locationLink;

    private String notes;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Feature feature;

    private CategoryResponse category;
}
