package com.abc.api.payload.request.products;

import com.abc.api.entities.Category;
import com.abc.api.entities.Feature;
import com.abc.api.entities.Location;
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
    private String title;

    private String subtitle;

    private BigDecimal priceMin;

    private BigDecimal priceMax;

    private String unit;

    private String description;

    private String locationLink;

    private String notes;

    private Feature feature;

    private Category category;

    private Location location;

    private Long createdBy;
}
