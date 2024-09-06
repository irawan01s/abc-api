package com.abc.api.payload.request.products;

import com.abc.api.entities.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductUpdateRequest {
    private String title;

    private String subtitle;

    private Long category_id;

    private BigDecimal priceMin;

    private BigDecimal priceMax;

    private String unit;

    private String description;

    private String location;

    private String notes;

    private Category category;
}
