package com.jacobo.reservation_system.repositories;

import com.jacobo.reservation_system.models.entities.Reservations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * JPA repository for the Reservations entity
 * Manages system reservations persistence
 */
public interface ReservationsRepository extends JpaRepository<Reservations, Long> {

    // Spring Data JPA parse SQL keywords named on the method below
    // And Hibernate builds consultation in the database automatically
    // With those keywords parsed by Spring Data JPA
    /**
     * Method that consults in the database
     * A reservations where matches a user and the state
     * is "active"
     * @param user_id
     * @param status
     * @return number of active reservations from a user
     */
    long countByUserIdAndStatus(Long user_id, String status);

    // When it's a more extended consultation
    // It's better to use @Query to create a personalized one
    // Not to set a long method name like the previous one
    /**
     * Method that consults in the database
     * If there's a date overlapping where
     * A reservation has a same resource associated
     * @param resource_id
     * @param new_start_date
     * @param new_end_date
     * @return List of overlapped reservations
     */
    @Query("""
    select r
    from Reservations r
    where r.resource.id = :resource_id
      and r.start_date < :new_end_date
      and r.end_date > :new_start_date
      and r.status = "active"
    """)
    List<Reservations> findOverlappingReservations(
            @Param("resource_id") Long resource_id,
            @Param("new_start_date") LocalDate new_start_date,
            @Param("new_end_date") LocalDate new_end_date
    );

    List<Reservations> findByUserId(Long id);
}
