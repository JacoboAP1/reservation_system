package com.jacobo.reservation_system.models.dtos.UserDtos;

import lombok.Data;

/**
 * OutDTO for users to return success message
 * when deactivating his state
 */
@Data
public class DeactivateUserOutDTO {
    private boolean success;
    private String message;
}
