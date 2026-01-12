package com.jacobo.reservation_system.services.implementation;

import com.jacobo.reservation_system.exceptions.UserNotFoundException;
import com.jacobo.reservation_system.models.dtos.UserDtos.GetAllUsersOutDTO;
import com.jacobo.reservation_system.models.dtos.UserDtos.GetUserByIdOutDTO;
import com.jacobo.reservation_system.models.entities.Roles;
import com.jacobo.reservation_system.models.entities.Users;
import com.jacobo.reservation_system.repositories.UsersRepository;
import com.jacobo.reservation_system.services.IUserService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service where is all business logic
 * User management
 * Implements his interface
 */
@Service
public class UserService implements IUserService {
    /**
     * It brings JPA methods
     */
    private final UsersRepository userRepo;

    /**
     * Injection of userRepo
     * Access to the methods
     * @param userRepo
     */
    public UserService(UsersRepository userRepo) {
        this.userRepo = userRepo;
    }

    /**
     * Method that shows a user
     * @return user's dto
     */
    @Override
    public GetUserByIdOutDTO getUserById(Long id) {
        Users user = userRepo.findById(id).orElseThrow(
                () -> new UserNotFoundException("Please register")
        );

        GetUserByIdOutDTO dto = new GetUserByIdOutDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setActive(user.getActive());
        dto.setRole(
                user.getRoles().stream().map(
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
                            users.getRoles().stream().map(
                                    Roles::getName
                            ).toList()
                    );

                    dto.setActive(users.getActive());

                    return dto;
                }
        ).toList();
    }

}
