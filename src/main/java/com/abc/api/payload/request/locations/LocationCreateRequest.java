package com.abc.api.payload.request.locations;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationCreateRequest {

    private Long id;

    private String name;

    private String link;

    private String address;
}
