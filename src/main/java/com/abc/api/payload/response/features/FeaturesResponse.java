package com.abc.api.payload.response.features;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeaturesResponse {
    private Long id;

    private String name;

    private String description;
}
