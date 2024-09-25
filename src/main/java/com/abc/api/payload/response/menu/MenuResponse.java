package com.abc.api.payload.response.menu;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuResponse {

    private Long id;

    private Long parentId;

    private String name;

    private String description;

    private String icon;

    private String url;

    private Integer sequence;
}
