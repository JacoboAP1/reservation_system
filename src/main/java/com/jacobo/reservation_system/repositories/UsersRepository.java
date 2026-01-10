package com.jacobo.reservation_system.repositories;

import com.jacobo.reservation_system.models.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * JPA repository for the Users entity
 * Manages system user persistence, including searches by name or email
 */
public interface UsersRepository extends JpaRepository<Users,Long> {
    Optional<Users> findByUsername(String username);
}

