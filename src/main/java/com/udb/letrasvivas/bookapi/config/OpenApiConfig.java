package com.udb.letrasvivas.bookapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
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
                        .description("API RESTful para la gestión de libros en Letras Vivas. "
                                + "Permite crear, leer, actualizar y eliminar libros del catálogo.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipo de Desarrollo UDB")
                                .email("desarrollo@udb.edu.sv")
                                .url("https://www.udb.edu.sv"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Servidor de Desarrollo"),
                        new Server()
                                .url("https://api.letrasvivas.udb.edu.sv")
                                .description("Servidor de Producción")
                ));
    }
}
