package com.jacobo.reservation_system.exceptions;

public class RoleNotValidException extends RuntimeException {
    public RoleNotValidException(String message) {
        super(message);
    }
}
