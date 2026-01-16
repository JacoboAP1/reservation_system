package com.jacobo.reservation_system.exceptions.AuthExceptions;

public class UserDeactivationException extends RuntimeException {
    public UserDeactivationException(String message) {
        super(message);
    }
}
