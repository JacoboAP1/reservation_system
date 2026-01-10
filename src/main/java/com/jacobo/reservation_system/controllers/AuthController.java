package com.jacobo.reservation_system.controllers;

import com.jacobo.reservation_system.exceptions.InvalidEmailException;
import com.jacobo.reservation_system.exceptions.MissFillingFieldsException;
import com.jacobo.reservation_system.exceptions.UserNotFoundException;
import com.jacobo.reservation_system.exceptions.UsernameAlreadyExistsException;
import com.jacobo.reservation_system.models.dtos.RegisterRequest;
import com.jacobo.reservation_system.models.entities.Roles;
import com.jacobo.reservation_system.models.entities.Users;
import com.jacobo.reservation_system.repositories.RolesRepository;
import com.jacobo.reservation_system.repositories.UsersRepository;
import com.jacobo.reservation_system.services.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import io.swagger.v3.oas.annotations.Operation; // Importa las librerías para documentar endpoints con etiquetas de swagger
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controlador para la autenticación y registro de usuarios.
 * Sistema con un único rol ("USER") según requerimientos del proyecto GenoSentinel.
 */
@Tag(name = "Autenticación", description = "Endpoints para el registro y login de usuarios") // Swagger annotation
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    /**
     * AuthenticationManager de Spring Security para autenticar usuarios.
     */
    private final AuthenticationManager authManager;
    /**
     * Repositorio para la gestión de usuarios.
     */
    private final UsersRepository usuarioRepo;
    /**
     * Repositorio para la gestión de roles.
     */
    private final RolesRepository rolRepo;
    /**
     * Servicio para la gestión de JWT.
     */
    private final JwtService jwt;
    /**
     * PasswordEncoder para encriptar contraseñas.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Endpoint de login de usuario.
     * Valida credenciales, correo y genera un token JWT.
     */
    @Operation (
            summary = "Iniciar sesión",
            description = "Autentica al usuario con su nombre de usuario, correo y contraseña. Retorna un token JWT si las credenciales son válidas"
    ) //Swagger annotation
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Autenticación exitosa, retorna token JWT"),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas"),
            @ApiResponse(responseCode = "400", description = "Solicitud mal formada"),
            @ApiResponse(responseCode = "404", description = "No se encontró el nombre de usuario")
    }) //Swagger annotation
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> req) {
        String username = req.get("username");
        String email = req.get("email");
        String password = req.get("password");

        if (username == null || username.isBlank()
                || email == null || email.isBlank()
                || password == null || password.isBlank()) {

            throw new MissFillingFieldsException("Fill all the fields");
        }

        // Busca usuario en base de datos
        var user = usuarioRepo.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("El nombre de usuario no existe"));

        // Valida que el email coincida
        if (!email.equals(user.getEmail())) {
            throw new InvalidEmailException("El correo electrónico no coincide con el registrado.");
        }

        // Si las credenciales son incorrectas, lanza AuthenticationException → 401 por defecto
        authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        var roles = user.getRoles().stream().map(Roles::getName).toList();

        String token = jwt.generate(user.getUsername(), roles);

        return Map.of(
                "access_token", token,
                "token_type", "Bearer",
                "roles", roles
        );
    }

    /**
     * Endpoint para registrar nuevos usuarios.
     * Crea usuario con rol único "USER" y devuelve token JWT.
     */
    @Operation(
            summary = "Registrar nuevo usuario",
            description = "Crea un nuevo usuario con su nombre, correo y contraseña. Retorna un token JWT al registrarse correctamente"
    ) //Swagger annotation
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o faltantes"),
            @ApiResponse(responseCode = "409", description = "El nombre de usuario ya existe")
    }) //Swagger annotation
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> register(@RequestBody RegisterRequest req) {
        if (req.getUsername() == null || req.getUsername().isBlank()
                || req.getEmail() == null || req.getEmail().isBlank()
                ||  req.getPassword() == null || req.getPassword().isBlank()) {

            throw new MissFillingFieldsException("Fill all the fields");
        }
        if (usuarioRepo.findByUsername(req.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException("Enter another username");
        }

        List<String> roleNames = (req.getRoles() == null || req.getRoles().isEmpty())
                ? List.of("USER")
                : req.getRoles();

        // Manejo correcto de roles para evitar ConcurrentModificationException
        Set<Roles> roleEntities = new HashSet<>();
        for (String roleName : roleNames) {
            Roles role = rolRepo.findByName(roleName).orElseGet(() -> {
                Roles newRole = new Roles();
                newRole.setName(roleName);
                return rolRepo.save(newRole);
            });
            roleEntities.add(role);
        }

        Users user = new Users();
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRoles(roleEntities);

        usuarioRepo.save(user);

        List<String> roles = roleEntities.stream().map(Roles::getName).toList();
        String token = jwt.generate(user.getUsername(), roles);

        return Map.of(
                "access_token", token,
                "token_type", "Bearer",
                "roles", roles
        );
    }

    /**
     * Maneja errores de autenticación devolviendo un mensaje estándar.
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(org.springframework.security.core.AuthenticationException.class)
    public Map<String, String> onAuthError(Exception e) {
        return Map.of("error", "Bad credentials");
    }
}

