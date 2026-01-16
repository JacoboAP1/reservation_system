package com.jacobo.reservation_system.exceptions.AuthExceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
