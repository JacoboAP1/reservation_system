package com.jacobo.reservation_system.services;

import com.jacobo.reservation_system.models.dtos.ResourcesDtos.CreateResourceInDTO;
import com.jacobo.reservation_system.models.dtos.ResourcesDtos.CreateResourcesOutDTO;
import com.jacobo.reservation_system.models.dtos.ResourcesDtos.DeactivateResourceOutDTO;
import com.jacobo.reservation_system.models.dtos.ResourcesDtos.ListAllResourcesOutDTO;

import java.util.List;

/**
 * Resources service interface that defines methods to implement
 */
public interface IResourcesService {
    public CreateResourcesOutDTO createResource(CreateResourceInDTO inDto);
    public List<ListAllResourcesOutDTO> listAllResources();
    public DeactivateResourceOutDTO deactivateResource(Long id);
}
