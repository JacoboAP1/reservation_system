package com.jacobo.reservation_system.swagger;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger/OpenAPI configuration for the authentication module
 * Define the general API information and register the security scheme
 * JWT-based to enable the "Authorize" button in the Swagger interface
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        // Definition of the Bearer authentication scheme with JWT
        // to allow Swagger to send the token in the Authorization header
        SecurityScheme securityScheme = new SecurityScheme()
                .name("bearer-jwt")
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("Enter the token in the format: Bearer {token}");

        return new OpenAPI()
                .info(new Info()
                        .title("Reservation management system")
                        .description("JWT authentication API")
                        .version("1.0.0")
                )
                .components(new Components()
                        .addSecuritySchemes("bearer-jwt", securityScheme)
                )
                .addSecurityItem(new SecurityRequirement().addList("bearer-jwt"));

    }
}
