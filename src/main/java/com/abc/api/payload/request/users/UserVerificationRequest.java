package com.abc.api.payload.request.users;

import lombok.Data;

@Data
public class UserVerificationRequest {
    private String email;

    private String token;
}
