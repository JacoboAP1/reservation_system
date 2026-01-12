package com.jacobo.reservation_system.services;

import com.jacobo.reservation_system.models.dtos.UserDtos.GetAllUsersOutDTO;
import com.jacobo.reservation_system.models.dtos.UserDtos.GetUserByIdOutDTO;
import java.util.List;

/**
 * Service interface that defines methods to implement
 */
public interface IUserService {
    public GetUserByIdOutDTO getUserById(Long id);
    public List<GetAllUsersOutDTO> getUsers();
}
