package com.jacobo.reservation_system.services;

import com.jacobo.reservation_system.models.dtos.UserDtos.*;

import java.util.List;

/**
 * User service interface that defines methods to implement
 */
public interface IUserService {
    public GetUserByIdOutDTO getUserById(Long id);
    public List<GetAllUsersOutDTO> getUsers();
    public DeactivateUserOutDTO deactivateUser(Long id);
    public DeleteUserOutDTO deleteUser(Long id);
}
