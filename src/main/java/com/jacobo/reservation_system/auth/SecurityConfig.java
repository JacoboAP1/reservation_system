package com.jacobo.reservation_system.auth;

// Imports required for security configuration
import lombok.RequiredArgsConstructor; // Automatically generates the constructor with the fields marked as 'final'
import org.springframework.context.annotation.Bean; // It allows you to define methods that return objects managed by Spring
import org.springframework.context.annotation.Configuration; // Indicates that this class contains Spring configuration
import org.springframework.security.authentication.AuthenticationManager; // Manage the authentication process
import org.springframework.security.authentication.AuthenticationProvider; // Define how authentication is performed
import org.springframework.security.authentication.ProviderManager; // Concrete implementation of AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider; // Provider that uses database data for authentication
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity; // It allows the use of security annotations in methods
import org.springframework.security.config.annotation.web.builders.HttpSecurity; // Allows you to configure the security of HTTP requests
import org.springframework.security.config.http.SessionCreationPolicy; // Define how sessions are managed
import org.springframework.security.core.userdetails.UserDetailsService; // Service for obtaining user information
import org.springframework.security.crypto.password.PasswordEncoder; // Encrypt and verify passwords
import org.springframework.security.web.SecurityFilterChain; // Chain of filters that process requests
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // Filter for authentication by username and password
import org.springframework.web.cors.CorsConfiguration; // Configuration to allow requests from other domains
import org.springframework.web.cors.CorsConfigurationSource; // CORS configuration source
import org.springframework.web.cors.UrlBasedCorsConfigurationSource; // CORS source based on URL routes
import java.util.List; // Utility for lists

/**
 * This class configures the application's security
 * This is where you define the filters, access rules, authentication provider, and CORS configuration
 * Role-based permissions are assigned using annotations (@PreAuthorize) in the controllers
 */
@Configuration // This indicates that this class is for Spring configuration
@EnableMethodSecurity // It allows the use of annotations such as @PreAuthorize in controller methods
@RequiredArgsConstructor // Generate the constructor with the 'final' fields automatically
public class SecurityConfig {
    // A filter that validates the JWT on each request. If the token is valid, it allows access
    private final JwtAuthFilter jwtFilter;
    // Service that retrieves user data from the database
    private final UserDetailsService uds;
    // Password encoder, for example BCrypt. It is used to securely store and verify passwords
    private final PasswordEncoder encoder;

    /**
     * Configure the security filter chain and access rules
     * - Disable CSRF because it is not used in REST APIs
     * - Configure CORS to allow requests from the frontend
     * - Defines that sessions are not used (stateless)
     * - It allows public access to log in and registration
     * - The remaining endpoints require authentication
     * - Role-based permissions are assigned in controllers using @PreAuthorize
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // Disable CSRF protection (not required for REST APIs)
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) //  Apply the CORS configuration defined below
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // No sessions are used, each request is validated on its own
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                // It grants endpoint access permissions to users with credentials
                                // created or allows free access to Swagger UI and documentation
                                // endpoints. Otherwise, no one else
                                "/auth/login",
                                "/auth/register",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .anyRequest().authenticated()
                )

                // Use the authentication provider defined below
                .authenticationProvider(authenticationProvider())
                // Add the JWT filter before the standard username/password filter
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build(); // Build and return the filter chain
    }

    /**
     * Define the authentication provider
     * This provider uses the user service and password encoder to validate credentials
     */
    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(); // Provider that uses the database for authentication
        provider.setUserDetailsService(uds); // Use the service to obtain user data
        provider.setPasswordEncoder(encoder); // Use the encoder to verify the password
        return provider; // Returns the configured provider
    }

    /**
     * Define the authentication manager
     * The manager uses the authentication provider to process login attempts
     */
    @Bean
    AuthenticationManager authenticationManager() {
        return new ProviderManager(authenticationProvider()); // Use the provider defined above
    }

    /**
     * Configure CORS to allow requests from the frontend (e.g., React or Angular)
     * This defines the allowed origins, methods, and headers
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration(); // Create CORS config
        // It allows requests from these origins (frontend URLs)
        cfg.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:4200", "http://127.0.0.1:3000"));
        // Allow these HTTP methods
        cfg.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
        // Allow these headers in requests
        cfg.setAllowedHeaders(List.of("Authorization","Content-Type","Accept"));
        // Expose the Authorization header in the responses
        cfg.setExposedHeaders(List.of("Authorization"));
        // Allows sending credentials (cookies, tokens)
        cfg.setAllowCredentials(true);
        // Defines how long the CORS configuration is cached in the browser
        cfg.setMaxAge(3600L);
        // Apply the configuration to all API routes
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }
}
