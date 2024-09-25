package com.abc.api.services;

import com.abc.api.entities.Menu;
import com.abc.api.entities.User;
import com.abc.api.exceptions.ResourceNotFoundException;
import com.abc.api.payload.request.menu.MenuCreateRequest;
import com.abc.api.payload.request.menu.MenuUpdateRequest;
import com.abc.api.payload.response.menu.MenuResponse;
import com.abc.api.repositories.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    public List<MenuResponse> getAll() {
        return menuRepository.findAll()
                .stream()
                .map(this::toMenuResponse)
                .toList();
    }

    @Transactional
    public MenuResponse create(User user, MenuCreateRequest request) {
        Menu menu = new Menu();
        menu.setParentId(request.getParentId());
        menu.setName(request.getName());
        menu.setDescription(request.getDescription());
        menu.setIcon(request.getIcon());
        menu.setUrl(request.getUrl());
        menu.setSequence(request.getSequence());
        menu.setCreatedBy(user.getId());

        menuRepository.save(menu);
        return toMenuResponse(menu);
    }

    @Transactional
    public void update(User user,Long id, MenuUpdateRequest request) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu not found"));

        menu.setParentId(request.getParentId());
        menu.setName(request.getName());
        menu.setDescription(request.getDescription());
        menu.setIcon(request.getIcon());
        menu.setUrl(request.getUrl());
        menu.setSequence(request.getSequence());
        menu.setCreatedBy(user.getId());

        menuRepository.save(menu);
    }

    public void delete(Long id) {
        menuRepository.findById(id).ifPresentOrElse(menuRepository::delete, () -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu not found");
        });
    }

    private MenuResponse toMenuResponse(Menu menu) {
        return MenuResponse.builder()
                .id(menu.getId())
                .parentId(menu.getParentId())
                .name(menu.getName())
                .description(menu.getDescription())
                .icon(menu.getIcon())
                .url(menu.getUrl())
                .sequence(menu.getSequence())
                .build();
    }
}
