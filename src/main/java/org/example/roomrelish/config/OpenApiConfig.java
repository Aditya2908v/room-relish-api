package org.example.roomrelish.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Room Relish Api",
                description = "API documentation for the Room Relish Hotel Booking System.",
                version = "1.0",
                contact = @Contact(
                        name = "Aditya Reddy",
                        email = "aditya.sureshkumarreddy.ciec@skytv.it"
                )
        ),
        servers = {
                @Server(
                        url = "http://localhost:8081",
                        description = "Local Development Server"
                )
        },
        security = {
                @SecurityRequirement(
                        name="bearerAuth"
                )
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT authentication",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
