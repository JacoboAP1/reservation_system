package com.jacobo.reservation_system.services.implementation;

import com.jacobo.reservation_system.exceptions.AuthExceptions.UserDeactivationException;
import com.jacobo.reservation_system.exceptions.AuthExceptions.UserNotFoundException;
import com.jacobo.reservation_system.models.dtos.UserDtos.*;
import com.jacobo.reservation_system.models.entities.Roles;
import com.jacobo.reservation_system.models.entities.Users;
import com.jacobo.reservation_system.repositories.UsersRepository;
import com.jacobo.reservation_system.services.IUsersService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service where is all business logic
 * User management
 * Implements his interface
 */
@Service
public class UsersService implements IUsersService {
    /**
     * It brings JPA methods
     */
    private final UsersRepository userRepo;

    /**
     * Injection of userRepo
     * Access to the methods
     * @param userRepo
     */
    public UsersService(UsersRepository userRepo) {
        this.userRepo = userRepo;
    }

    /**
     * Method that shows a user
     * @return user's dto
     */
    @Override
    public GetUsersByIdOutDTO getUserById(Long id) {
        Users user = userRepo.findById(id).orElseThrow(
                () -> new UserNotFoundException("Enter an existing id")
        );

        GetUsersByIdOutDTO dto = new GetUsersByIdOutDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setActive(user.getActive());
        dto.setRole(
                user.getRole().stream().map(
                        Roles::getName
                ).toList()
        );

        return dto;
    }

    /**
     * Method that lists all users
     * @return List of users
     */
    @Override
    public List<GetAllUsersOutDTO> getUsers() {
        List<Users> entity = userRepo.findAll();

        return entity.stream().map(
                users -> {
                    GetAllUsersOutDTO dto = new GetAllUsersOutDTO();

                    dto.setId(users.getId());
                    dto.setUsername(users.getUsername());
                    dto.setRole(
                            users.getRole().stream().map(
                                    Roles::getName
                            ).toList()
                    );

                    dto.setActive(users.getActive());

                    return dto;
                }
        ).toList();
    }

    /**
     * Method that deactivates the user state
     * @param id
     * @return outDto
     */
    @Override
    public DeactivateUsersOutDTO deactivateUser(Long id) {
        Users user = userRepo.findById(id).
                orElseThrow(() -> new UserNotFoundException("Enter an existing ID"));

        DeactivateUsersOutDTO outDto = new DeactivateUsersOutDTO();

        if (!user.getActive()) {
            throw new UserDeactivationException("User is already deactivated");
        }

        user.setActive(false);
        userRepo.save(user);

        outDto.setMessage("User deactivated successfully");
        outDto.setSuccess(true);

        return outDto;
    }

    /**
     * Method that deletes a user searching for his id
     * @param id
     * @return outDTO
     */
    @Override
    public DeleteUsersOutDTO deleteUser(Long id) {
        Users user = userRepo.findById(id).
                orElseThrow(() -> new UserNotFoundException("Enter an existing ID"));

        userRepo.delete(user);

        DeleteUsersOutDTO outDTO = new DeleteUsersOutDTO();
        outDTO.setSuccess(true);
        outDTO.setMessage("User deleted successfully");

        return outDTO;
    }

}
