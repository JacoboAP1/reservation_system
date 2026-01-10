package com.jacobo.reservation_system.models.entities;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Entity that represents a user role within the system
 * Roles determine access permissions and are used in authentication
 * and authorization via JWT
 * It is mapped to the "roles" table
 */
@Entity
@Table(name = "roles")
@Data
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;
}

