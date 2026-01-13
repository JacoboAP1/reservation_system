package com.jacobo.reservation_system.exceptions;

public class UserDeactivationException extends RuntimeException {
    public UserDeactivationException(String message) {
        super(message);
    }
}
