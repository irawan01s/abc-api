package com.abc.api.services;

import com.abc.api.payload.request.users.SearchUserRequest;
import com.abc.api.payload.request.users.UserCreateRequest;
import com.abc.api.payload.request.users.UserUpdateRequest;
import com.abc.api.payload.response.users.UserResponse;
import com.abc.api.entities.User;
import com.abc.api.repositories.UserRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse create(UserCreateRequest request) {
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
        }

        user.setName(request.getName());
        user.setAddress(request.getAddress());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPosition(request.getPosition());
        user.setDivision(request.getDivision());

        userRepository.save(user);

        return toUserResponse(user);
    }
}
