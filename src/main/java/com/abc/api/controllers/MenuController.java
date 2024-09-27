package com.abc.api.controllers;

import com.abc.api.entities.User;
import com.abc.api.payload.request.menu.MenuCreateRequest;
import com.abc.api.payload.request.menu.MenuUpdateRequest;
import com.abc.api.payload.response.WebResponse;
import com.abc.api.payload.response.menu.MenuResponse;
import com.abc.api.services.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/menus")
public class MenuController {

    private final MenuService menuService;

    @GetMapping
    public WebResponse<List<MenuResponse>> getAll() {
        List<MenuResponse> menus = menuService.getAll();
        return WebResponse.<List<MenuResponse>>builder()
                .status(true)
                .data(menus)
                .build();
    }

    @GetMapping("/{id}")
    public WebResponse<MenuResponse> getById(@PathVariable Long id) {
        MenuResponse menu = menuService.getById(id);
        return WebResponse.<MenuResponse>builder()
                .status(true)
                .data(menu)
                .build();
    }

    @PostMapping
    public WebResponse<MenuResponse> createMenu(@AuthenticationPrincipal User user, @RequestBody MenuCreateRequest request) {
        MenuResponse menu = menuService.create(user, request);

        return WebResponse.<MenuResponse>builder()
                .status(true)
                .data(menu)
                .build();
    }

    @PutMapping(path = "/{id}")
    public WebResponse<Object> updateMenu(@AuthenticationPrincipal User user, @PathVariable Long id, @RequestBody MenuUpdateRequest request) {
        menuService.update(user, id, request);

        return WebResponse.builder()
                .status(true)
                .build();
    }

    @DeleteMapping(path = "/{id}")
    public WebResponse<Object> deleteMenu(@PathVariable Long id) {
        menuService.delete(id);

        return WebResponse.builder()
                .status(true)
                .build();
    }
}
