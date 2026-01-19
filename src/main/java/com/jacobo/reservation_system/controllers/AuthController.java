package com.jacobo.reservation_system.controllers;

import com.jacobo.reservation_system.exceptions.AuthExceptions.*;
import com.jacobo.reservation_system.exceptions.MissFillingFieldsException;
import com.jacobo.reservation_system.models.dtos.AuthDtos.RegisterRequest;
import com.jacobo.reservation_system.models.entities.Roles;
import com.jacobo.reservation_system.models.entities.Users;
import com.jacobo.reservation_system.repositories.RolesRepository;
import com.jacobo.reservation_system.repositories.UsersRepository;
import com.jacobo.reservation_system.services.implementation.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import io.swagger.v3.oas.annotations.Operation; // Imports libraries to document endpoints with Swagger annotations
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller for authentication and user registration
 * System with role USER and ADMIN according to the project requirements
 */
@Tag(name = "Authentication", description = "Endpoints register and login for user") // Swagger annotation
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    /**
     * AuthenticationManager from Spring Security to authenticate users
     */
    private final AuthenticationManager authManager;
    /**
     * Repository for user management
     */
    private final UsersRepository userRepo;
    /**
     * Repository for role management
     */
    private final RolesRepository roleRepo;
    /**
     * Service for JWT management
     */
    private final JwtService jwt;
    /**
     * PasswordEncoder to encrypt passwords.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * User login endpoint
     * Validates credentials, email and generates a JWT token
     */
    @Operation (
            summary = "Login",
            description = "Authenticate the user with his username, email and password " +
                    "Return a JWT token if the credentials are valid"
    ) //Swagger annotation
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful authentication, returns JWT token"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Username was not found")
    }) //Swagger annotation
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> req) {
        String username = req.get("username");
        String email = req.get("email");
        String password = req.get("password");

        if (username == null || username.isBlank()
                || email == null || email.isBlank()
                || password == null || password.isBlank()) {

            throw new MissFillingFieldsException("Complete all the fields");
        }

        // Search for users in the database
        var user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Username does not exist"));

        if (!user.getActive()) {
            throw new UserDeactivationException("Not possible to log in because the user is " +
                    "deactivated");
        }

        // Check for email coincidence
        if (!email.equals(user.getEmail())) {
            throw new InvalidEmailException("The email does not match with the registered one");
        }

        // If the credentials are incorrect, throws AuthenticationException → 401 by default
        authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        var roles = user.getRole().stream().map(Roles::getName).toList();
        String token = jwt.generate(user.getUsername(), roles);

        return Map.of(
                "access_token", token,
                "token_type", "Bearer",
                "roles", roles
        );
    }

    /**
     * Endpoint to register new users
     * Creates user with role USER or ADMIN and returns a JWT token
     */
    @Operation(
            summary = "Register new user",
            description = "Creates a new user with his username, email and password. " +
                    "Returns a JWT token by successful registration"
    ) //Swagger annotation
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid or missed data"),
            @ApiResponse(responseCode = "409", description = "Username already exists")
    }) //Swagger annotation
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> register(@RequestBody RegisterRequest req) {
        if (req.getUsername() == null || req.getUsername().isBlank()
                || req.getEmail() == null || req.getEmail().isBlank()
                ||  req.getPassword() == null || req.getPassword().isBlank()) {

            throw new MissFillingFieldsException("Fill all the fields");
        }
        if (userRepo.findByUsername(req.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException("Enter another username");
        }

        List<String> roleNames = (req.getRoles() == null || req.getRoles().isEmpty())
                ? List.of("USER")
                : req.getRoles();

        if (roleNames.size() > 1) {
            throw new MultipleRolesException("Maybe you may undo this restriction " +
                    "for projects in the future");
        }

        // Correct management of role to avoid ConcurrentModificationException
        Set<Roles> roleEntities = new HashSet<>();
        for (String roleName : roleNames) {
            Roles role = roleRepo.findByName(roleName).orElseGet(() -> {
                if (roleName.equals("USER") || roleName.equals("ADMIN")) {
                    Roles newRole = new Roles();
                    newRole.setName(roleName);
                    return roleRepo.save(newRole);
                }

                throw new RoleNotValidException("You must register as ADMIN or USER");
            });

            roleEntities.add(role);
        }

        Users user = new Users();
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRole(roleEntities);

        userRepo.save(user);

        List<String> roles = roleEntities.stream().map(Roles::getName).toList();
        String token = jwt.generate(user.getUsername(), roles);

        return Map.of(
                "access_token", token,
                "token_type", "Bearer",
                "roles", roles
        );
    }

    /**
     * Manage authentication errors returning a standard message
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(org.springframework.security.core.AuthenticationException.class)
    public Map<String, String> onAuthError(Exception e) {
        return Map.of("error", "Bad credentials");
    }
}

