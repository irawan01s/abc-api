package com.abc.api.controllers;

import com.abc.api.payload.request.locations.LocationCreateRequest;
import com.abc.api.payload.response.WebResponse;
import com.abc.api.payload.response.locations.LocationResponse;
import com.abc.api.services.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/locations")
public class LocationController {

    private final LocationService locationService;

    @GetMapping
    public WebResponse<List<LocationResponse>> getAll() {
        List<LocationResponse> locations = locationService.getAll();
        return WebResponse.<List<LocationResponse>>builder()
                .status(true)
                .data(locations)
                .build();
    }

    @PostMapping
    public WebResponse<LocationResponse> createLocation(@RequestBody LocationCreateRequest request) {
        LocationResponse location = locationService.create(request);

        return WebResponse.<LocationResponse>builder()
                .status(true)
                .data(location)
                .build();
    }
}
