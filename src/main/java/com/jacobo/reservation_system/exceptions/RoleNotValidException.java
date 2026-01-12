package com.jacobo.reservation_system.exceptions;

/**
 * When we set a non permitted role at registering moment
 */
public class RoleNotValidException extends RuntimeException {
    public RoleNotValidException(String message) {
        super(message);
    }
}
