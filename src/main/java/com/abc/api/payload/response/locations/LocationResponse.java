package com.abc.api.payload.response.locations;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationResponse {

    private Long id;

    private String name;

    private String link;

    private String address;
}
