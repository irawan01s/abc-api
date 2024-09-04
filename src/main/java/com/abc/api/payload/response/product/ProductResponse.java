package com.abc.api.payload.response.product;

import com.abc.api.entities.Category;
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

    private BigDecimal price;

    private String unit;

    private String description;

    private String location;

    private String notes;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Category category;
}
