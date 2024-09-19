package com.abc.api.repositories;

import com.abc.api.entities.Feature;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeatureRepository extends JpaRepository <Feature, Long> {
}
