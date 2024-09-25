package com.abc.api.payload.request.menu;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuCreateRequest {

    private Long parentId;

    private String name;

    private String description;

    private String icon;

    private String url;

    private Integer sequence;
}
