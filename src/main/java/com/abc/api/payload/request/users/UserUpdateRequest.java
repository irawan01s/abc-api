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

    private String password;

    private Integer nik;

    private String name;

    private String address;

    private String email;

    private String phone;

    private String position;

    private String division;
}
