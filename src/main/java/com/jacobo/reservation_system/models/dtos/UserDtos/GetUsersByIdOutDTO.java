package com.jacobo.reservation_system.models.dtos.UserDtos;

import lombok.Data;
import java.util.List;

/**
 * Users out DTO for exposing specific info
 * about a specific user
 */
@Data
public class GetUsersByIdOutDTO {
    private Long id;
    private String username;
    private Boolean active;
    private List<String> role;
}
