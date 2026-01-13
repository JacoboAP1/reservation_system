package com.jacobo.reservation_system.exceptions;

import com.jacobo.reservation_system.models.dtos.UserDtos.DeactivateUserOutDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.security.access.AccessDeniedException;
import java.util.Map;

/**
 * Global manager for custom exceptions
 * Returns JSON responses readable by Postman or HTTP clients
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidEmail(InvalidEmailException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(
                        "error", "Invalid email",
                        "message", ex.getMessage()
                ));
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleExistingUser(UsernameAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "error", "Username already exists",
                        "message", ex.getMessage()
                ));
    }

    @ExceptionHandler(MissFillingFieldsException.class)
    public ResponseEntity<Map<String, Object>> handleMissingFields(MissFillingFieldsException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "error", "Username or password or email empty",
                        "message", ex.getMessage()
                ));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "error", "Not user found with that ID",
                        "message", ex.getMessage()
                ));
    }

    // Bad request of role when registering
    @ExceptionHandler(RoleNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleRoleNotValid(RoleNotValidException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "error", "Role not valid",
                        "message", ex.getMessage()
                ));
    }

    // Spring security exception caught here to throw another HTTP status
    // Not 403 forbidden
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(
                        "error", "Invalid role for that action",
                        "message", "Only ADMIN users can access this resource"
                ));
    }

    @ExceptionHandler(UserDeactivationException.class)
    public ResponseEntity<DeactivateUserOutDTO> handleAccessDenied(UserDeactivationException ex) {
        DeactivateUserOutDTO outDto = new DeactivateUserOutDTO();
        outDto.setSuccess(false);
        outDto.setMessage(ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.CONFLICT).body(outDto);
    }
}

