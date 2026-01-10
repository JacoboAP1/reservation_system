package com.jacobo.reservation_system.exceptions;

public class MissFillingFieldsException extends RuntimeException {
    public MissFillingFieldsException(String message) {
        super(message);
    }
}
