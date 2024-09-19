package com.abc.api.payload.response.users;

import lombok.Data;

@Data
public class UserVerificationResponse {

    private String email;

    private String token;
}
