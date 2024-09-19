package com.abc.api.services;

import com.abc.api.entities.Location;
import com.abc.api.payload.request.locations.LocationCreateRequest;
import com.abc.api.payload.response.locations.LocationResponse;
import com.abc.api.repositories.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    public List<LocationResponse> getAll() {
        return locationRepository.findAll()
                .stream()
                .map(this::toLocationResponse)
                .toList();
    }

    @Transactional
    public LocationResponse create(LocationCreateRequest request) {
        Location location = new Location();
        location.setName(request.getName());
        location.setLink(request.getLink());
        location.setAddress(request.getAddress());

        locationRepository.save(location);

        return toLocationResponse(location);
    }

    private LocationResponse toLocationResponse(Location location) {
        return LocationResponse.builder()
                .id(location.getId())
                .name(location.getName())
                .link(location.getLink())
                .address(location.getAddress())
                .build();
    }
}
