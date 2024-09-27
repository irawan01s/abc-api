package com.abc.api.services;

import com.abc.api.payload.request.EmailRequest;
import com.abc.api.payload.request.auth.AuthRequest;
import com.abc.api.payload.request.users.UserCreateRequest;
import com.abc.api.entities.User;
import com.abc.api.payload.request.users.UserVerificationRequest;
import com.abc.api.payload.response.users.UserResponse;
import com.abc.api.repositories.UserRepository;
import com.abc.api.utils.EmailHandler;
import jakarta.mail.MessagingException;
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

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final EmailHandler emailHandler;

    @Transactional
    public UserResponse signup(UserCreateRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already registered");
        }
        LocalDateTime tokenExpired =LocalDateTime.now().plusMinutes(10);

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
        user.setType(request.getType());
        user.setStatus(request.getStatus());
        user.setIsVerified(false);
        user.setToken(generateVerificationCode());
        user.setTokenExpiredAt(tokenExpired);
        sendVerificationEmail(user);
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
                .type(user.getType())
                .status(user.getStatus())
                .isVerified(user.getIsVerified())
                .token(user.getToken())
                .tokenExpiredAt(user.getTokenExpiredAt())
                .build();
    }

    public User login(AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return user;
    }

    public void verifiedUser(UserVerificationRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (user.getTokenExpiredAt().isBefore(LocalDateTime.now())) {
            System.out.println("Token Is Expired");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Verification code has expired");
        }
        System.out.println(user.getToken() + " Sama / Tidak" + request.getToken());
        if (user.getToken().equals(request.getToken())) {
            user.setStatus(1);
            user.setIsVerified(true);
            user.setToken(null);
            user.setTokenExpiredAt(null);
            userRepository.save(user);
        } else {
            System.out.println("Token Tidak Sama");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid verification code");
        }
    }

    public void resendVerificationCode(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (user.getIsVerified()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account is already verified");
        }

        user.setToken(generateVerificationCode());
        user.setTokenExpiredAt(LocalDateTime.now().plusHours(1));
        sendVerificationEmail(user);

        userRepository.save(user);
    }

    public void sendVerificationEmail(User user) {
        String subject = "[ABC] Account Verification";
        String verificationCode = user.getToken();
        EmailRequest message = new EmailRequest();
        message.setTitle("Please enter the verification code below to continue: ");
        message.setContent("<h3 style=\"color: #333;\">Verification Code:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>");

        try {
            emailHandler.sendEmail(user.getEmail(), subject, message);
        } catch (MessagingException e) {
            // Handle email sending exception
            e.printStackTrace();
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }
}
