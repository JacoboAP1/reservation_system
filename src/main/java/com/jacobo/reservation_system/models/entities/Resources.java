package com.jacobo.reservation_system.models.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "resources")
@Data
public class Resources {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Column(nullable = false, unique = true, length = 100,
            name = "name") // Name único y obligatorio
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "active")
    private boolean active;
}
