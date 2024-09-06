package com.abc.api.controllers;

import com.abc.api.payload.request.auth.AuthRequest;
import com.abc.api.payload.request.users.UserCreateRequest;
import com.abc.api.payload.response.WebResponse;
import com.abc.api.payload.response.auth.AuthResponse;
import com.abc.api.entities.User;
import com.abc.api.payload.response.users.UserResponse;
import com.abc.api.services.AuthService;
import com.abc.api.services.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final JwtService jwtService;

    private final AuthService authService;

    public AuthController(JwtService jwtService, AuthService authService) {
        this.jwtService = jwtService;
        this.authService = authService;
    }

    @PostMapping("/signup")
    public WebResponse<UserResponse> register(@RequestBody UserCreateRequest request) {
        UserResponse user = authService.signup(request);

        return WebResponse.<UserResponse>builder().data(user).build();
    }

    @PostMapping("/login")
    public WebResponse<AuthResponse> login(@RequestBody AuthRequest request) {
        System.out.println(request);
        User user = authService.login(request);

        String jwtToken = jwtService.generateToken(user);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(jwtToken);
        authResponse.setExpiresIn(jwtService.getExpirationTime());

        return WebResponse.<AuthResponse>builder().data(authResponse).build();
    }
}
