package com.jacobo.reservation_system.exceptions.AuthExceptions;

public class InvalidEmailException extends RuntimeException {
    public InvalidEmailException(String message) {
        super(message);
    }
}
