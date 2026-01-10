package com.jacobo.reservation_system.repositories;

import com.jacobo.reservation_system.models.entities.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio JPA para la entidad Role.
 * Gestiona los roles de usuario y proporciona métodos para buscar roles por nombre o identificador.
 */
public interface RolesRepository extends JpaRepository<Roles, Long> {
    Optional<Roles> findByName(String name);
}
