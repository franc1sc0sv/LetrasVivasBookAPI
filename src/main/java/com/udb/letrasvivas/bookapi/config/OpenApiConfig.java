package com.udb.letrasvivas.bookapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.port:8081}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Letras Vivas Book API")
                        .description("A comprehensive RESTful API for managing books in the Letras Vivas library system. "
                                + "This API provides full CRUD operations, advanced search capabilities, pagination, "
                                + "and comprehensive validation. Built with Spring Boot, Hibernate, and OpenAPI documentation.")
                        .version("2.0.0")
                        .contact(new Contact()
                                .name("UDB Development Team")
                                .email("development@udb.edu.sv")
                                .url("https://www.udb.edu.sv"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Development Server"),
                        new Server()
                                .url("https://api.letrasvivas.udb.edu.sv")
                                .description("Production Server")
                ))
                .tags(List.of(
                        new Tag()
                                .name("Book Management")
                                .description("Operations for managing books in the library catalog"),
                        new Tag()
                                .name("Search & Discovery")
                                .description("Advanced search and filtering capabilities"),
                        new Tag()
                                .name("Statistics")
                                .description("Library statistics and analytics")
                ))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT token authentication")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}
