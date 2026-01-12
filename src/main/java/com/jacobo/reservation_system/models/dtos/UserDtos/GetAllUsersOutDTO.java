package com.jacobo.reservation_system.models.dtos.UserDtos;

import lombok.Data;
import java.util.List;

/**
 * Users out DTO for exposing specific info
 * about all users
 */
@Data // Getters and Setters, constructor
public class GetAllUsersOutDTO {
    private Long id;
    private String username;
    private Boolean active;
    private List<String> role;
}
