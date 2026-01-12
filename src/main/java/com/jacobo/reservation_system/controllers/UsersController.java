package com.jacobo.reservation_system.controllers;

import com.jacobo.reservation_system.models.dtos.UserDtos.GetAllUsersOutDTO;
import com.jacobo.reservation_system.models.dtos.UserDtos.GetUserByIdOutDTO;
import com.jacobo.reservation_system.services.implementation.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * Controller to intercept client's HTTP requests
 * It delegates business logic to the service
 * Returns body and HTTP response to the client
 */
@Tag(name = "Users", description = "Endpoints for user's management") // Swagger annotation
@RestController
@RequestMapping("/users")
public class UsersController {
    /**
     * It invokes user service
     * It delegates business logic on the methods below
     */
    private final UserService userService;

    /**
     * We inject user service inside the constructor
     * @param userService
     */
    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Get users",
            description = "Returns full list of users"
    ) //Swagger annotation
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Return of users list successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized role for that action")
    }) //Swagger annotation
    @GetMapping("/obtain_users")
    @PreAuthorize("hasRole('ADMIN')")
    public List<GetAllUsersOutDTO> getUsersList() {
        return userService.getUsers();
    }

    @Operation(
            summary = "Get user by ID",
            description = "Return a specific user"
    ) //Swagger annotation
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Info returned successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized role for that action")
    }) //Swagger annotation
    @GetMapping("/obtain_user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public GetUserByIdOutDTO getUsersById(@PathVariable("id") Long id) {
        return userService.getUserById(id);
    }
}
