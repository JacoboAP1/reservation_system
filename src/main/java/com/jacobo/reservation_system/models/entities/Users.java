package com.jacobo.reservation_system.models.entities;

// Imports for JPA, Lombok, and Spring Security
import jakarta.persistence.*; // Annotations for entities and relationships
import lombok.Data; // Generate getters/setters, equals, hashCode, toString
import org.springframework.security.core.GrantedAuthority; // Represents an authority (role)
import org.springframework.security.core.userdetails.UserDetails; // Interface for authenticated users
import java.util.Collection; // Collection for roles
import java.util.HashSet; // Set Implementation
import java.util.Set; // Interface for roles
import java.util.stream.Collectors; // Utility for transforming collections

/**
 * Entity that represents a user of the system
 * Each user can have one or more assigned roles and is used
 * in the authentication and authorization process using JWT
 * It is mapped to the "users" table in the database
 */
@Entity // Mark the class as a JPA entity
@Table(name = "users") // Define the table in the database
@Data // Lombok: Generates getters/setters and other methods
public class Users implements UserDetails { // Implement UserDetails for integration with Spring Security
    @Id // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autoincrement
    private Long id; // Unique user identifier

    @Column(nullable = false, unique = true, length = 100) // Unique and mandatory username
    private String username; // Username

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false) // Mandatory password
    private String password; // Encrypted password

    /**
     * Hibernate ORM translates this data type to tinyint
     * automatically to MySQL
     */
    private Boolean active = true; // Indicates if the user is active
                                    // True by default

    @ManyToMany(fetch = FetchType.EAGER) // Many-to-many relationships with roles, immediate loading
    @JoinTable(
            name = "user_role", // Intermediate table
            joinColumns = @JoinColumn(name = "user_id"), // User FK
            inverseJoinColumns = @JoinColumn(name = "role_id") // Role FK
    )
    private Set<Roles> role = new HashSet<>(); // Set of roles assigned to the user

    @OneToMany(mappedBy = "user") // Attribute name from Reservations (user)
    private Set<Reservations> reservations;

    // Returns the user's authority (role) for Spring Security
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Transform each role into an authority with the prefix "ROLE_"
        return role.stream()
                .map(r -> (GrantedAuthority) () -> "ROLE_" + r.getName())
                .collect(Collectors.toSet());
    }

    // Methods required by UserDetails for account control
    @Override public boolean isAccountNonExpired() { return true; } // The account never expires
    @Override public boolean isAccountNonLocked() { return true; } // The account is never blocked
    @Override public boolean isCredentialsNonExpired() { return true; } // Credentials never expire
    @Override public boolean isEnabled() { return active; } // The user is enabled if active is true
}

