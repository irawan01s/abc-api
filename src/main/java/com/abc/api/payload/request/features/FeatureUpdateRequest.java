package com.abc.api.payload.request.features;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeatureUpdateRequest {

    private Long id;

    private String name;

    private String description;

    private Long updatedBy;
}
