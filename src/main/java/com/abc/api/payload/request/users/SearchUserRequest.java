package com.abc.api.payload.request.users;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchUserRequest {

    private Integer nik;

    private String name;

    @NotNull
    private Integer page;

    @NotNull
    private Integer size;
}
