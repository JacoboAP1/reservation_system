package com.jacobo.reservation_system.models.dtos.AuthDtos;

import lombok.Data;
import java.util.List;

/**
 * Attributes names defined below must be the same
 * On Postman's JSON
 */
@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    private List<String> roles;
}

