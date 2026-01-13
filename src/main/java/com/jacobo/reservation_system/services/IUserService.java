package com.jacobo.reservation_system.services;

import com.jacobo.reservation_system.models.dtos.UserDtos.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Service interface that defines methods to implement
 */
public interface IUserService {
    public GetUserByIdOutDTO getUserById(Long id);
    public List<GetAllUsersOutDTO> getUsers();
    public DeactivateUserOutDTO deactivateUser(Long id);
    public DeleteUserOutDTO deleteUser(Long id);
}
