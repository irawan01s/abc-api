package com.abc.api.services;

import com.abc.api.entities.Feature;
import com.abc.api.entities.User;
import com.abc.api.exceptions.ResourceNotFoundException;
import com.abc.api.payload.request.features.FeatureCreateRequest;
import com.abc.api.payload.request.features.FeatureUpdateRequest;
import com.abc.api.repositories.FeatureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeatureService {

    private final FeatureRepository featureRepository;

    public List<Feature> getAll() {
        return featureRepository.findAll();
    }

    @Transactional
    public Feature create(User user, FeatureCreateRequest request) {
        Feature feature = new Feature();
        feature.setName(request.getName());
        feature.setDescription(request.getName());
        feature.setCreatedBy(user.getId());

        return featureRepository.save(feature);
    }

    public Feature update(User user, Long id, FeatureUpdateRequest request) {
        Feature feature = featureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feature not found"));

        feature.setName(request.getName());
        feature.setDescription(request.getDescription());
        feature.setUpdatedBy(user.getId());
        return featureRepository.save(feature);
    }
}
