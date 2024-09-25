package com.abc.api.services;

import com.abc.api.payload.request.EmailRequest;
import com.abc.api.payload.request.users.SearchUserRequest;
import com.abc.api.payload.request.users.UserCreateRequest;
import com.abc.api.payload.request.users.UserResetPasswordRequest;
import com.abc.api.payload.request.users.UserUpdateRequest;
import com.abc.api.payload.response.users.UserResponse;
import com.abc.api.entities.User;
import com.abc.api.repositories.UserRepository;
import com.abc.api.utils.EmailHandler;
import jakarta.mail.MessagingException;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    @Value("${web.url}")
    private String webUrl;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final EmailHandler emailHandler;

    @Transactional
    public UserResponse create(UserCreateRequest request) {
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

        return toUserResponse(user);
    }

    public Page<UserResponse> getAll(SearchUserRequest request) {
        Specification<User> specification = (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(Objects.nonNull(request.getName())) {
                predicates.add(builder.like(root.get("name"), "%" + request.getName() + "%"));
            }

            if(Objects.nonNull(request.getNik())) {
                predicates.add(builder.like(root.get("nik"), "%" + request.getNik() + "%"));
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<User> users = userRepository.findAll(specification, pageable);

        List<UserResponse> userResponses = users.getContent().stream()
                .map(this::toUserResponse)
                .toList();

        return new PageImpl<>(userResponses, pageable, users.getTotalElements());
    }

    private UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
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

    public UserResponse update(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (Objects.nonNull(request.getPassword())) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        } else {
            user.setName(request.getName());
            user.setAddress(request.getAddress());
            user.setEmail(request.getEmail());
            user.setPhone(request.getPhone());
            user.setPosition(request.getPosition());
            user.setDivision(request.getDivision());
        }

        userRepository.save(user);

        return toUserResponse(user);
    }

    public void forgotPassword(String email) throws MessagingException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        String subject = "[ABC] Forgot Password";
        String token = UUID.randomUUID().toString();
        LocalDateTime tokenExpired =LocalDateTime.now().plusMinutes(30);

        user.setToken(token);
        user.setTokenExpiredAt(tokenExpired);
        userRepository.save(user);

        EmailRequest message = new EmailRequest();
        message.setTitle("Hi "+ user.getName() +",\n"
                + "please continue the password reset process via the following link.");
        message.setContent("<h3 style=\"color: #333;\"> You can click this link bellow</h3>"
                + "<p style=\"color: #007bff;\">" + webUrl +"/user/reset-password/" + user.getId() + "?token=" + token + "</p>");

        emailHandler.sendEmail(user.getEmail(), subject, message);

//        return webUrl +"/user/reset-password/" + user.getId() + "?token=" + token;
    }

    public void resetPassword(UserResetPasswordRequest request) {
        User user = userRepository.findByToken(request.getToken())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setToken(null);
        user.setTokenExpiredAt(null);
        userRepository.save(user);
    }
}
