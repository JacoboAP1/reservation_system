package com.jacobo.reservation_system.exceptions.ReservationsExceptions;

public class ReservationAlreadyCanceled extends RuntimeException {
    public ReservationAlreadyCanceled(String message) {
        super(message);
    }
}
