package com.jacobo.reservation_system.repositories;

import com.jacobo.reservation_system.models.entities.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * JPA Repository for Role entity
 * Manages user roles and provides methods to search for roles by name or identifier
 */
public interface RolesRepository extends JpaRepository<Roles, Long> {
    Optional<Roles> findByName(String name);
}
