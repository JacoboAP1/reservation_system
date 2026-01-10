package com.jacobo.reservation_system.auth;

import com.jacobo.reservation_system.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * JWT authentication filter for Spring Security
 * It intercepts each HTTP request and validates the JWT token present in the Authorization header
 * If the token is valid, it establishes authentication in the security context with the user roles
 * If the token is invalid or not present, the request continues without authentication
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    /**
     * Service for the management and validation of JWT tokens
     */
    private final JwtService jwtService;

    /**
     * Constructor that injects the JWT service
     * @param jwtService service to parse and validate tokens
     */
    public JwtAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    /**
     * Main method of the filter that intercepts each HTTP request
     * Extract the JWT token from the Authorization header, validate it, and establish authentication
     * If the token is invalid, clear the security context
     * @param req HTTP request
     * @param res HTTP response
     * @param chain filter chain
     * @throws ServletException if an error occurs in the filter
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        // Extract the Authorization header
        String header = req.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(req, res);
            return;
        }

        // Extract the JWT token
        String token = header.substring(7);
        try {
            // Parse and validate the token
            io.jsonwebtoken.Claims claims = jwtService.parse(token);
            String username = claims.getSubject();

            @SuppressWarnings("unchecked")
            List<String> roles = (List<String>) claims.get("roles");

            // Convert roles into Spring Security authorities
            List<SimpleGrantedAuthority> authorities =
                    (roles == null ? List.<String>of() : roles).stream()
                            .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r)
                            .map(SimpleGrantedAuthority::new)
                            .toList();

            // Create the authentication token and set it in the context
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(username, "N/A", authorities);

            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (Exception e) {
            //If the token is invalid, clear the security context
            SecurityContextHolder.clearContext();
        }

        //Continue with the filter chain
        chain.doFilter(req, res);
    }
}

