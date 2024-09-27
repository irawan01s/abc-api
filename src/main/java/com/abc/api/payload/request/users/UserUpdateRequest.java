package com.abc.api.payload.request.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateRequest {

    private String username;

    private String email;

    private String password;

    private Integer nik;

    private String name;

    private String address;

    private String phone;

    private String position;

    private String division;

    private String type;

    private Integer status;

    private Boolean isVerified;

    private Long updatedBy;
}
