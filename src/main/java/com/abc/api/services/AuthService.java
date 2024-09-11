package com.abc.api.services;

import com.abc.api.payload.request.auth.AuthRequest;
import com.abc.api.payload.request.users.UserCreateRequest;
import com.abc.api.entities.User;
import com.abc.api.payload.response.users.UserResponse;
import com.abc.api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    @Transactional
    public UserResponse signup(UserCreateRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already registered");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setNik(request.getNik());
        user.setName(request.getName());
        user.setAddress(request.getAddress());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setPosition(request.getPosition());
        user.setDivision(request.getDivision());
        userRepository.save(user);

        return UserResponse.builder()
                .username(user.getUsername())
                .nik(user.getNik())
                .name(user.getName())
                .address(user.getAddress())
                .email(user.getEmail())
                .phone(user.getPhone())
                .position(user.getPosition())
                .division(user.getDivision())
                .build();
    }

    public User login(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return userRepository.findByEmail(request.getEmail())
                .orElseThrow();
    }
}
