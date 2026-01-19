package com.jacobo.reservation_system.exceptions.AuthExceptions;

public class MultipleRolesException extends RuntimeException {
    public MultipleRolesException(String message) {
        super(message);
    }
}
