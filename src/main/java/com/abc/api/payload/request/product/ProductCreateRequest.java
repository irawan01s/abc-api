package com.abc.api.payload.request.product;

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
public class ProductCreateRequest {
    private Long id;

    private String title;

    private String subtitle;

    private Long category_id;

    private BigDecimal price;

    private String unit;

    private String description;

    private String location;

    private String notes;

    private Category category;
}
