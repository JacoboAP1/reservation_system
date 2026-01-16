package com.jacobo.reservation_system.exceptions.ResourcesExceptions;

public class ResourceAlreadyCreatedException extends RuntimeException {
    public ResourceAlreadyCreatedException(String message) {
        super(message);
    }
}
