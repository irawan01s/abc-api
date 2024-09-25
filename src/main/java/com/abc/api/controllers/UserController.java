package com.abc.api.controllers;

import com.abc.api.entities.User;
import com.abc.api.payload.request.users.*;
import com.abc.api.payload.response.PagingResponse;
import com.abc.api.payload.response.WebResponse;
import com.abc.api.payload.response.users.UserResponse;
import com.abc.api.services.UserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public WebResponse<UserResponse> create(@AuthenticationPrincipal User user, @RequestBody UserCreateRequest request) {
        UserResponse userResponse = userService.create(request);
        return WebResponse.<UserResponse>builder()
                .status(true).data(userResponse).build();
    }

    @GetMapping
    public WebResponse<List<UserResponse>> getAll(@RequestParam(value = "nik", required = false) Integer nik,
                                                    @RequestParam(value = "name", required = false) String name,
                                                    @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                    @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
            SearchUserRequest request = SearchUserRequest.builder()
                    .page(page)
                    .size(size)
                    .nik(nik)
                    .name(name)
                    .build();

            Page<UserResponse> userResponses = userService.getAll(request);

            return WebResponse.<List<UserResponse>>builder()
                    .status(true)
                    .data(userResponses.getContent())
                    .paging(PagingResponse.builder()
                            .currentPage(userResponses.getNumber())
                            .totalPage(userResponses.getTotalPages())
                            .size(userResponses.getSize())
                            .build())
                    .build();

    }

    @PutMapping(path = "/{id}")
    public WebResponse<UserResponse> update(@PathVariable("id") Long id, @RequestBody UserUpdateRequest request) {
        UserResponse userResponse = userService.update(id, request);
        return  WebResponse.<UserResponse>builder().data(userResponse).build();
    }

    @PostMapping(path = "/forgot-password")
    public WebResponse<Object> forgotPassword(@RequestBody UserForgotPasswordRequest request) throws MessagingException {
        userService.forgotPassword(request.getEmail());
        return WebResponse.builder()
                .status(true)
                .build();
    }

    @PutMapping(path = "/reset-password")
    public WebResponse<Object> resetPassword(@RequestBody UserResetPasswordRequest request) {
        userService.resetPassword(request);
        return WebResponse.builder()
                .status(true)
                .build();
    }
}
