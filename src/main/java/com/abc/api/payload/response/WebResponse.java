package com.abc.api.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WebResponse<T>{
    private Boolean status;

    private T data;

    private String errors;

    private PagingResponse paging;
}
