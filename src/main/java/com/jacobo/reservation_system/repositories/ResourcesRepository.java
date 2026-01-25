package com.jacobo.reservation_system.repositories;

import com.jacobo.reservation_system.models.entities.Resources;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * JPA Repository for Resources entity
 */
public interface ResourcesRepository extends JpaRepository<Resources, Long> {
    Optional<Resources> findByName(String name);
}
