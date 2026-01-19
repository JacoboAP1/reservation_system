package com.jacobo.reservation_system.exceptions.ReservationsExceptions;

public class ReservationDuplicatedException extends RuntimeException {
    public ReservationDuplicatedException(String message) {
        super(message);
    }
}
