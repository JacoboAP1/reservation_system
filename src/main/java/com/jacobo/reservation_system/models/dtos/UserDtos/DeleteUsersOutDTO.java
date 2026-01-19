package com.jacobo.reservation_system.models.dtos.UserDtos;

import lombok.Data;

@Data
public class DeleteUsersOutDTO {
    private boolean success;
    private String message;
}
