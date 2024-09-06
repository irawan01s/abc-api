package com.abc.api.payload.request.products;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductSearchRequest {
    private String title;

    private String subtitle;

    private Integer page;

    private Integer size;
}
