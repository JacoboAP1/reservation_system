package com.jacobo.reservation_system.models.dtos.ResourcesDtos;

import lombok.Data;

@Data
public class CreateResourceInDTO {
    private String name;
    private String description;
    private boolean active = true;
}
