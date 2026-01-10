package com.jacobo.reservation_system.services;

// Import the classes needed to work with JWT
import io.jsonwebtoken.Claims; // It represents the claims (information) within the JWT token
import io.jsonwebtoken.Jwts; // It represents the claims (information) within the JWT token
import io.jsonwebtoken.io.Decoders; // Utility to decode the secret key in base64
import io.jsonwebtoken.security.Keys; // Utility to generate the secret key
import org.springframework.beans.factory.annotation.Value; // It allows injecting values from application.properties
import org.springframework.stereotype.Service; // Mark the class as a Spring service
import javax.crypto.SecretKey; // Secret key to sign/verify JWT
import java.time.Instant; // It represents a moment in time
import java.util.Date; // Issue date and expiration
import java.util.List; // List of roles
import java.util.Map; // Map for custom claims

/**
 * Service for managing JWT tokens
 * It allows generating and validating tokens for authentication and authorization
 * Use a secret key and a configurable expiration time
 */
@Service // Indicates that this class is a service managed by Spring
public class JwtService {

    /**
     * Secret key used to sign and verify JWT tokens
     */
    private final SecretKey key;
    /**
     * Token expiration time in minutes
     */
    private final long expMinutes;

    /**
     * Constructor that initializes the secret key and expiration time
     * @param secret secret key in base64 or plain text
     * @param expMinutes token expiration minutes
     */
    public JwtService(@Value("${jwt.secret}") String secret,
                      @Value("${jwt.exp-min}") long expMinutes) {
        // Decode the secret key from base64 if applicable, or use it as plain text
        byte[] raw = secret.matches("^[A-Za-z0-9+/=]+$") ? Decoders.BASE64.decode(secret) : secret.getBytes();
        // Generates the secret key to sign/verify JWT
        this.key = Keys.hmacShaKeyFor(raw);
        //Save the configured expiration time
        this.expMinutes = expMinutes;
    }

    /**
     * Generate a JWT token with the subject and roles provided
     * @param subject user identifier (username)
     * @param roles user role list
     * @return token JWT signed
     */
    public String generate(String subject, List<String> roles) {
        Instant now = Instant.now(); // Gets the current time
        List<String> safeRoles = roles == null ? List.of() : roles; // Ensure that the role list is not null
        // Build the JWT token
        return Jwts.builder()
                .subject(subject) // Set the subject (username)
                .claims(Map.of("roles", safeRoles)) // Add the roles as a custom claim
                .issuedAt(Date.from(now)) // Issue date
                .expiration(Date.from(now.plusSeconds(expMinutes * 60))) // Expiration date
                // Sign the token with the secure key and algorithm
                .signWith(key)
                .signWith(key, Jwts.SIG.HS256)
                .compact(); // Ends and returns the JWT token
    }

    /**
     * Validates and parses a JWT token, returning its claims
     * @param token JWT token to validate
     * @return claims extracted from the token
     */
    public Claims parse(String token) {
        // Remove the "Bearer" prefix if it is present
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        // Parse and validate the JWT token, returning the claims
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}

