package com.jacobo.reservation_system.services;

import com.jacobo.reservation_system.models.dtos.ResourcesDtos.CreateResourcesInDTO;
import com.jacobo.reservation_system.models.dtos.ResourcesDtos.CreateResourcesOutDTO;
import com.jacobo.reservation_system.models.dtos.ResourcesDtos.DeactivateResourcesOutDTO;
import com.jacobo.reservation_system.models.dtos.ResourcesDtos.ListAllResourcesOutDTO;

import java.util.List;

/**
 * Resources service interface that defines methods to implement
 */
public interface IResourcesService {
    public CreateResourcesOutDTO createResource(CreateResourcesInDTO inDto);
    public List<ListAllResourcesOutDTO> listAllResources();
    public DeactivateResourcesOutDTO deactivateResource(Long id);
}
