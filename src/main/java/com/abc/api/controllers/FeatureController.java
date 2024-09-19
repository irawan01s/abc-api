package com.abc.api.controllers;

import com.abc.api.entities.Feature;
import com.abc.api.entities.User;
import com.abc.api.payload.request.features.FeatureCreateRequest;
import com.abc.api.payload.request.features.FeatureUpdateRequest;
import com.abc.api.payload.response.WebResponse;
import com.abc.api.services.FeatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/features")
public class FeatureController {

    private final FeatureService featureService;

    @GetMapping
    public WebResponse<List<Feature>> getAll() {
        List<Feature> features = featureService.getAll();
        return WebResponse.<List<Feature>>builder()
                .status(true)
                .data(features)
                .build();
    }

    @PostMapping
    public WebResponse<Feature> createFeature(@AuthenticationPrincipal User user, @RequestBody FeatureCreateRequest request) {
        Feature feature = featureService.create(user, request);

        return WebResponse.<Feature>builder()
                .status(true)
                .data(feature)
                .build();
    }

    @PutMapping("/{id}")
    public WebResponse<Feature> updateFeature(@AuthenticationPrincipal User user, @PathVariable Long id,@RequestBody FeatureUpdateRequest request) {
        Feature feature = featureService.update(user, id, request);
        return WebResponse.<Feature>builder()
                .status(true)
                .data(feature)
                .build();
    }
}
