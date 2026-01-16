package com.jacobo.reservation_system.exceptions.AuthExceptions;

public class MissFillingFieldsException extends RuntimeException {
    public MissFillingFieldsException(String message) {
        super(message);
    }
}
