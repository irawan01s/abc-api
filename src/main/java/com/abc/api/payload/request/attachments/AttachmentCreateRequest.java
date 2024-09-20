package com.abc.api.payload.request.attachments;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttachmentCreateRequest {

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
