package com.abc.api.controllers;

import com.abc.api.payload.request.auth.AuthRequest;
import com.abc.api.payload.request.users.UserCreateRequest;
import com.abc.api.payload.response.WebResponse;
import com.abc.api.payload.response.auth.AuthResponse;
import com.abc.api.entities.User;
import com.abc.api.payload.response.users.UserResponse;
import com.abc.api.services.AuthService;
import com.abc.api.services.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<WebResponse<UserResponse>> signup(@RequestBody UserCreateRequest request) {
        UserResponse user = authService.signup(request);
        return ResponseEntity.ok(WebResponse.<UserResponse>builder()
                        .status(true)
                        .data(user)
                        .build());
    }

    @PostMapping("/login")
    public ResponseEntity<WebResponse<AuthResponse>> login(@RequestBody AuthRequest request) {
        User user = authService.login(request);

        String jwtToken = jwtService.generateToken(user);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(jwtToken);
        authResponse.setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(WebResponse.<AuthResponse>builder()
                    .status(true)
                    .data(authResponse).build());
    }
}
