package com.jacobo.reservation_system.models.dtos.ReservationsDtos;

import lombok.Data;

import java.time.LocalDate;

/**
 * InDTO to create a reservation
 * It doesn't need user_id because we obtain it by asking
 * To Spring Security for the authenticated user
 * -----------------------------------------------------
 * Attributes names defined below must be the same
 * On Postman's JSON
 * -----------------------------------------------------
 * NOTE: Check @ReservationsService
 */
@Data
public class CreateReservationsInDTO {
    private Long resource_id;
    private LocalDate start_date;
    private LocalDate end_date;
}
