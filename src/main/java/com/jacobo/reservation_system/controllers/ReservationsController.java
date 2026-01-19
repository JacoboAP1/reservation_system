package com.jacobo.reservation_system.controllers;

import com.jacobo.reservation_system.models.dtos.ReservationsDtos.CreateReservationsInDTO;
import com.jacobo.reservation_system.models.dtos.ReservationsDtos.CreateReservationsOutDTO;
import com.jacobo.reservation_system.services.implementation.ReservationsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for reservations management
 * Intercepts HTTP request from client and returns responses
 */
@Tag(name = "Reservations", description = "endpoints for reservations management")
@RestController
@RequestMapping("/reservations")
public class ReservationsController {
    private final ReservationsService reservationSer;

    public ReservationsController(ReservationsService reservationSer) {
        this.reservationSer = reservationSer;
    }

    @Operation (
            summary = "Create reservation",
            description = "Creates reservations filling all the required fields (Only USER)" +
                    "because is the client"
    ) //Swagger annotation
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reservation created successfully"),
            @ApiResponse(responseCode = "401", description = "Role not permitted"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "406", description = "Reservation is duplicated"),
            @ApiResponse(responseCode = "409", description = "Conflict. Already active reservation"),
            @ApiResponse(responseCode = "404", description = "Resource or user not found")

    }) //Swagger annotation
    @PostMapping("/create_reservation")
    @PreAuthorize("hasRole('USER')")
    public CreateReservationsOutDTO createReservation(@RequestBody CreateReservationsInDTO inDto) {
        return reservationSer.createReservations(inDto);
    }
}
