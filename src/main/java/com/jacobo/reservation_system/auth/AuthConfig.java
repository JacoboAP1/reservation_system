package com.jacobo.reservation_system.auth;

import com.jacobo.reservation_system.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Authentication and security settings for the application
 * Define beans for password encoding and loading user details
 * It uses BCrypt for password security and connects the user repository for authentication
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthConfig {
    /**
     * Repository for user management in authentication
     */
    private final UsersRepository usersRepository;

    /**
     * Bean for password encoding using BCrypt
     * @return Secure PasswordEncoder for storing passwords
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean to load user details from the database
     * Used by Spring Security to authenticate users
     * @return UserDetailsService that searches for users by username
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> usersRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}

