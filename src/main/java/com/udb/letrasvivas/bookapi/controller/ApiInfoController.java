package com.udb.letrasvivas.bookapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "API Information", description = "Información general sobre la API")
public class ApiInfoController {

    @GetMapping("/info")
    @Operation(
            summary = "Información de la API",
            description = "Proporciona información general sobre la API Letras Vivas Book"
    )
    public ResponseEntity<Map<String, Object>> getApiInfo() {
        Map<String, Object> apiInfo = new HashMap<>();
        apiInfo.put("name", "Letras Vivas Book API");
        apiInfo.put("version", "1.0.0");
        apiInfo.put("description", "API RESTful para la gestión de libros en Letras Vivas");
        apiInfo.put("author", "Equipo de Desarrollo UDB");
        apiInfo.put("contact", "desarrollo@udb.edu.sv");
        apiInfo.put("documentation", "/swagger-ui.html");
        apiInfo.put("openapi", "/api-docs");

        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("books", "/api/books");
        endpoints.put("search", "/api/books/search");
        endpoints.put("swagger-ui", "/swagger-ui.html");
        endpoints.put("openapi-json", "/api-docs");

        apiInfo.put("endpoints", endpoints);

        return ResponseEntity.ok(apiInfo);
    }

    @GetMapping("/health")
    @Operation(
            summary = "Estado de salud de la API",
            description = "Verifica que la API esté funcionando correctamente"
    )
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> health = new HashMap<>();
        health.put("status", "UP");
        health.put("message", "Letras Vivas Book API is running");
        health.put("timestamp", java.time.LocalDateTime.now().toString());

        return ResponseEntity.ok(health);
    }
}
