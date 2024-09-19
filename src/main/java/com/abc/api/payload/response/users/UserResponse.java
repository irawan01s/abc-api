package com.abc.api.payload.response.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

    private Long id;

    private String username;

    private Integer nik;

    private String name;

    private String address;

    private String email;

    private String phone;

    private String position;

    private String division;

    private String token;

    private LocalDateTime tokenExpiredAt;
}
