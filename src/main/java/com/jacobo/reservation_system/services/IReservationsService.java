package com.jacobo.reservation_system.services;

import com.jacobo.reservation_system.models.dtos.ReservationsDtos.CreateReservationsInDTO;
import com.jacobo.reservation_system.models.dtos.ReservationsDtos.CreateReservationsOutDTO;
import com.jacobo.reservation_system.models.dtos.ReservationsDtos.DeactivateReservationsOutDTO;
import com.jacobo.reservation_system.models.dtos.ReservationsDtos.GetAllReservationsOutDTO;

import java.util.List;

/**
 * Reservations service interface that defines methods to implement
 */
public interface IReservationsService {
    public CreateReservationsOutDTO createReservations(CreateReservationsInDTO inDto);
    public List<GetAllReservationsOutDTO> listReservations();
    public DeactivateReservationsOutDTO deactivateReservation(Long id);
}
