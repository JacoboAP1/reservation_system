package com.jacobo.reservation_system.controllers;

import com.jacobo.reservation_system.models.dtos.ResourcesDtos.CreateResourcesInDTO;
import com.jacobo.reservation_system.models.dtos.ResourcesDtos.CreateResourcesOutDTO;
import com.jacobo.reservation_system.models.dtos.ResourcesDtos.DeactivateResourcesOutDTO;
import com.jacobo.reservation_system.models.dtos.ResourcesDtos.ListAllResourcesOutDTO;
import com.jacobo.reservation_system.services.implementation.ResourcesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for resources management
 * Intercepts HTTP request from client and returns responses
 */
@Tag(name = "Resources", description = "Endpoints for resources management")
@RestController
@RequestMapping("/resources")
public class ResourcesController {
    private final ResourcesService resourcesServ;

    public ResourcesController (ResourcesService resourcesServ) {
        this.resourcesServ = resourcesServ;
    }

    /**
     * Resource creation endpoint
     */
    @Operation (
            summary = "Create resource",
            description = "Creates resources filling all the required fields (Only ADMIN)"
    ) //Swagger annotation
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Resource created successfully"),
            @ApiResponse(responseCode = "401", description = "Role not permitted"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "406", description = "Resource name already exists")
    }) //Swagger annotation
    @PostMapping("/create_resource")
    @PreAuthorize("hasRole('ADMIN')")
    public CreateResourcesOutDTO createResource(@RequestBody CreateResourcesInDTO inDto) {
        return resourcesServ.createResource(inDto);
    }

    @Operation(
            summary = "Get resources",
            description = "Returns full list of resources"
    ) //Swagger annotation
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Return of resources list successfully")
    }) //Swagger annotation
    @GetMapping("/")
    public List<ListAllResourcesOutDTO> getResourcesList() {
        return resourcesServ.listAllResources();
    }

    @Operation(
            summary = "Deactivate resource",
            description = "Deactivates resource by changing his state"
    ) //Swagger annotation
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success message returned"),
            @ApiResponse(responseCode = "409", description = "Failure message returned"),
            @ApiResponse(responseCode = "401", description = "Unauthorized role for that action")
    }) //Swagger annotation
    @PatchMapping("deactivate_resource/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public DeactivateResourcesOutDTO deactivateResource(@PathVariable("id") Long id) {
        return resourcesServ.deactivateResource(id);
    }

}
