package com.abc.api.controllers;

import com.abc.api.payload.request.users.SearchUserRequest;
import com.abc.api.payload.request.users.UserCreateRequest;
import com.abc.api.payload.request.users.UserUpdateRequest;
import com.abc.api.payload.response.PagingResponse;
import com.abc.api.payload.response.WebResponse;
import com.abc.api.payload.response.users.UserResponse;
import com.abc.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping()
    public WebResponse<UserResponse> create(@RequestBody UserCreateRequest request) {
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
}
