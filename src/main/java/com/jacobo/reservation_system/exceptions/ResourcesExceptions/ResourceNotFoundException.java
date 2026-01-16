package com.jacobo.reservation_system.exceptions.ResourcesExceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
