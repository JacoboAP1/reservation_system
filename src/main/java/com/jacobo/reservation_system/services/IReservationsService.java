package com.jacobo.reservation_system.services;

import com.jacobo.reservation_system.models.dtos.ReservationsDtos.CreateReservationsInDTO;
import com.jacobo.reservation_system.models.dtos.ReservationsDtos.CreateReservationsOutDTO;

/**
 * Reservations service interface that defines methods to implement
 */
public interface IReservationsService {
    public CreateReservationsOutDTO createReservations(CreateReservationsInDTO inDto);

}
