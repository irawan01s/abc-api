package com.abc.api.repositories;

import com.abc.api.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository <Location, Long> {
}
