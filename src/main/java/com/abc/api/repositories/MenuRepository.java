package com.abc.api.repositories;

import com.abc.api.entities.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository <Menu, Long> {
}
