package com.abc.api.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttachmentResponse {

    private Long id;

    private Long refId;

    private String refTable;

    private String document;

    private String name;

    private String type;

    private long size;

    private String path;

    private Boolean approved;

    private String notes;
}
