package com.jacobo.reservation_system.services.implementation;

import com.jacobo.reservation_system.exceptions.AuthExceptions.UserNotFoundException;
import com.jacobo.reservation_system.exceptions.MissFillingFieldsException;
import com.jacobo.reservation_system.exceptions.ResourcesExceptions.ResourceAlreadyCreatedException;
import com.jacobo.reservation_system.exceptions.ResourcesExceptions.ResourceDeactivationException;
import com.jacobo.reservation_system.exceptions.ResourcesExceptions.ResourceNotFoundException;
import com.jacobo.reservation_system.models.dtos.ResourcesDtos.CreateResourcesInDTO;
import com.jacobo.reservation_system.models.dtos.ResourcesDtos.CreateResourcesOutDTO;
import com.jacobo.reservation_system.models.dtos.ResourcesDtos.DeactivateResourcesOutDTO;
import com.jacobo.reservation_system.models.dtos.ResourcesDtos.ListAllResourcesOutDTO;
import com.jacobo.reservation_system.models.entities.Resources;
import com.jacobo.reservation_system.models.entities.Roles;
import com.jacobo.reservation_system.models.entities.Users;
import com.jacobo.reservation_system.repositories.ResourcesRepository;
import com.jacobo.reservation_system.repositories.UsersRepository;
import com.jacobo.reservation_system.services.IResourcesService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service where is all business logic
 * Resources management
 * Implements his interface
 */
@Service
public class ResourcesService implements IResourcesService {
    private final ResourcesRepository resourcesRepo;
    private final UsersRepository usersRepo;

    public ResourcesService (ResourcesRepository resourcesRepo,
                             UsersRepository usersRepo) {

        this.usersRepo = usersRepo;
        this.resourcesRepo = resourcesRepo;
    }

    /**
     * Method that implements resources creation
     * @param inDto
     * @return outDto
     */
    @Override
    public CreateResourcesOutDTO createResource(CreateResourcesInDTO inDto) {
        if (inDto.getName() == null || inDto.getName().isBlank()
            || inDto.getDescription() == null || inDto.getDescription().isBlank()) {

            throw new MissFillingFieldsException("Complete the creation info");
        }

        if (resourcesRepo.findByName(inDto.getName()).isPresent()) {
            throw new ResourceAlreadyCreatedException("Enter a different name");
        }

        Resources resource = new Resources();
        resource.setName(inDto.getName());
        resource.setDescription(inDto.getDescription());
        resource.setActive(true);

        resourcesRepo.save(resource);

        CreateResourcesOutDTO outDto = new CreateResourcesOutDTO();
        outDto.setName(resource.getName());
        outDto.setDescription(resource.getDescription());

        return outDto;
    }

    /**
     * Method that lists all the resources depending on user's role
     * If role's ADMIN lists both active and inactive resources
     * else if role's USER lists only active ones
     * In this case, the user is already authenticated, so we don't have
     * to request his ID parameter. We obtain it by asking to Spring Security
     * @return List<ListAllResourcesOutDto> outDto </ListAllResourcesOutDto>
     */
    @Override
    public List<ListAllResourcesOutDTO> listAllResources() {
        List<Resources> resource = resourcesRepo.findAll();
        List<Resources> adminRes = new ArrayList<>();
        List<Resources> userRes = new ArrayList<>();

        for (Resources res: resource) {
            if (res.isActive()) {
                userRes.add(res);
                adminRes.add(res);
            }
            else {
                adminRes.add(res);
            }
        }

        // Retrieves the current authentication object from Spring Security context
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Gets the username of the currently authenticated user
        String username = auth.getName();

        Users user = usersRepo.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException("Enter an existing username")
        );

        List<ListAllResourcesOutDTO> outDto = new ArrayList<>();

        for (Roles role: user.getRole()) {
            if (role.getName().equals("ADMIN")) {

                for (Resources res: adminRes) {
                    ListAllResourcesOutDTO dto = new ListAllResourcesOutDTO();
                    dto.setName(res.getName());
                    dto.setDescription(res.getDescription());

                    outDto.add(dto);
                }
            }
            else if (role.getName().equals("USER")) {

                for (Resources res: userRes) {
                    ListAllResourcesOutDTO dto = new ListAllResourcesOutDTO();
                    dto.setName(res.getName());
                    dto.setDescription(res.getDescription());

                    outDto.add(dto);
                }
            }
        }

        return outDto;
    }

    /**
     * Method that deactivates the resource state
     * @param id
     * @return outDto
     */
    @Override
    public DeactivateResourcesOutDTO deactivateResource(Long id) {
        Resources resource = resourcesRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Enter an existing ID")
        );

        DeactivateResourcesOutDTO dto = new DeactivateResourcesOutDTO();

        if (!resource.isActive()) {
            throw new ResourceDeactivationException("Resource is already deactivated");
        }

        resource.setActive(false);
        resourcesRepo.save(resource);

        dto.setSuccess(true);
        dto.setMessage("Resource deactivated successfully");

        return dto;
    }

}
