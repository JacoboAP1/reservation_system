package com.jacobo.reservation_system.models.dtos.ResourcesDtos;

import lombok.Data;

/**
 * Attributes names defined below must be the same
 * On Postman's JSON
 */
@Data
public class CreateResourcesInDTO {
    private String name;
    private String description;
}
