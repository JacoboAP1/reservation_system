package com.jacobo.reservation_system.services;

import com.jacobo.reservation_system.models.dtos.UserDtos.*;

import java.util.List;

/**
 * User service interface that defines methods to implement
 */
public interface IUsersService {
    public GetUsersByIdOutDTO getUserById(Long id);
    public List<GetAllUsersOutDTO> getUsers();
    public DeactivateUsersOutDTO deactivateUser(Long id);
    public DeleteUsersOutDTO deleteUser(Long id);
}
