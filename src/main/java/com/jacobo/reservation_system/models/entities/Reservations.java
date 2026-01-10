package com.jacobo.reservation_system.models.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
@Data
public class Reservations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user; // This is the attribute name mapped in Users
                        // by using @OneToMany

    @ManyToOne
    @JoinColumn(name = "resource_id")
    private Resources resource;

    @Column(name = "start_date_time")
    private LocalDateTime start_date_time;

    @Column(name = "end_date_time")
    private LocalDateTime end_date_time;

    @Column(name = "status")
    private String status;
}
