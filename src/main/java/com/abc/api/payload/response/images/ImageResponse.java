package com.abc.api.payload.response.images;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageResponse {
    private Long id;

    private String name;

    private long size;

    private String type;

    private Integer sequence;
}
