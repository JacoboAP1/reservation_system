package com.jacobo.reservation_system.exceptions.ReservationsExceptions;

public class ReservationNotFoundException extends RuntimeException {
    public ReservationNotFoundException(String message) {
        super(message);
    }
}
