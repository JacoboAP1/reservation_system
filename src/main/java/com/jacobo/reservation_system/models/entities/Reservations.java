package com.jacobo.reservation_system.models.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

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
    private LocalDate start_date; // Only date, not time
                                // For using date + time, import LocalDatetime

    @Column(name = "end_date_time")
    private LocalDate end_date;

    @Column(name = "status")
    private String status = "active"; // By default
}
