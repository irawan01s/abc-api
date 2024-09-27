package com.abc.api.payload.request.users;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCreateRequest {

    @NotBlank
    private Long id;

    private String username;

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    private Integer nik;

    @NotBlank
    private String name;

    private String address;

    private String phone;

    private String position;

    private String division;

    private String type;

    private Integer status;

    private Boolean isVerified;

    private Long createdBy;
}
