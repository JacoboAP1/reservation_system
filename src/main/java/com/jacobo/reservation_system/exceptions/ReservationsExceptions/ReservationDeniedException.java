package com.jacobo.reservation_system.exceptions.ReservationsExceptions;

public class ReservationDeniedException extends RuntimeException {
    public ReservationDeniedException(String message) {
        super(message);
    }
}
