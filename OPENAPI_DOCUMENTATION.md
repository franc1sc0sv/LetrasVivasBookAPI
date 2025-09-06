# 📚 Documentación OpenAPI y Swagger - Letras Vivas Book API

## 🎯 **Descripción General**

Este proyecto implementa **OpenAPI 3.0** y **Swagger UI** para proporcionar documentación interactiva y completa de la API REST. La documentación se genera automáticamente basándose en las anotaciones de código y la configuración personalizada.

## 🚀 **URLs de Acceso**

### **Desarrollo (Local)**
- **Swagger UI**: http://localhost:8081/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8081/api-docs
- **API Info**: http://localhost:8081/api/info
- **Health Check**: http://localhost:8081/api/health

### **Producción**
- **Swagger UI**: Deshabilitado por seguridad
- **OpenAPI JSON**: Deshabilitado por seguridad

## 📋 **Dependencias Implementadas**

### **Maven (pom.xml)**
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.3.0</version>
</dependency>
```

### **Características de la Dependencia**
- ✅ Soporte completo para OpenAPI 3.0
- ✅ Interfaz Swagger UI integrada
- ✅ Generación automática de documentación
- ✅ Compatible con Spring Boot 3.x
- ✅ Soporte para validación de esquemas

## ⚙️ **Configuración Implementada**

### **1. Clase de Configuración Principal**
```java
@Configuration
public class OpenApiConfig {
    // Configuración personalizada de OpenAPI
    // Información de contacto, licencia, servidores
}
```

### **2. Configuración en application.properties**
```properties
# Swagger/OpenAPI Configuration
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.operationsSorter=method
springdoc.swagger-ui.tagsSorter=alpha
springdoc.swagger-ui.doc-expansion=none
springdoc.swagger-ui.display-request-duration=true
springdoc.swagger-ui.filter=true
springdoc.swagger-ui.try-it-out-enabled=true
springdoc.swagger-ui.syntax-highlight.theme=monokai
```

## 🏷️ **Anotaciones OpenAPI Implementadas**

### **Anotaciones de Clase**
- `@Tag`: Agrupa operaciones por categoría
- `@Schema`: Define esquemas de datos y ejemplos

### **Anotaciones de Método**
- `@Operation`: Describe operaciones individuales
- `@ApiResponses`: Define respuestas posibles
- `@Parameter`: Documenta parámetros de entrada

### **Anotaciones de Respuesta**
- `@ApiResponse`: Define códigos de respuesta
- `@Content`: Especifica contenido de respuesta
- `@ExampleObject`: Proporciona ejemplos de datos

## 📖 **Ejemplos de Implementación**

### **Controlador con Documentación Completa**
```java
@RestController
@RequestMapping("/api/books")
@Tag(name = "Book Management", description = "APIs para la gestión de libros")
public class BookController {

    @GetMapping("/{id}")
    @Operation(
        summary = "Obtener libro por ID",
        description = "Recupera un libro específico por su ID",
        operationId = "getBookById"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Libro encontrado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Book.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Libro no encontrado"
        )
    })
    public ResponseEntity<Book> getBookById(
        @Parameter(description = "ID del libro", example = "1")
        @PathVariable Long id
    ) {
        // Implementación del método
    }
}
```

### **Modelo con Esquemas Documentados**
```java
@Entity
@Schema(description = "Entidad que representa un libro")
public class Book {

    @Schema(description = "Identificador único", example = "1")
    private Long id;

    @Schema(description = "Título del libro", example = "Don Quijote", required = true)
    private String title;

    @Schema(description = "Autor del libro", example = "Cervantes", required = true)
    private String author;
}
```

## 🔧 **Configuración de Entornos**

### **Desarrollo (application.properties)**
- Swagger UI habilitado
- Documentación completa disponible
- Logging detallado

### **Producción (application-prod.properties)**
- Swagger UI deshabilitado por seguridad
- Documentación deshabilitada
- Logging optimizado

## 🌐 **Características de Swagger UI**

### **Funcionalidades Disponibles**
- ✅ **Try it out**: Prueba endpoints directamente desde la UI
- ✅ **Filtrado**: Búsqueda y filtrado de operaciones
- ✅ **Ordenamiento**: Por método HTTP o alfabético
- ✅ **Temas**: Resaltado de sintaxis personalizable
- ✅ **Duración**: Muestra tiempo de respuesta
- ✅ **Validación**: Validación de esquemas en tiempo real

### **Personalización de UI**
- Tema de sintaxis: Monokai
- Expansión de documentación: Colapsada por defecto
- Ordenamiento: Por método HTTP
- Filtros habilitados

## 📱 **Endpoints de Información Adicional**

### **GET /api/info**
Proporciona información general sobre la API:
```json
{
  "name": "Letras Vivas Book API",
  "version": "1.0.0",
  "description": "API RESTful para la gestión de libros",
  "author": "Equipo de Desarrollo UDB",
  "contact": "desarrollo@udb.edu.sv",
  "documentation": "/swagger-ui.html",
  "openapi": "/api-docs",
  "endpoints": {
    "books": "/api/books",
    "search": "/api/books/search"
  }
}
```

### **GET /api/health**
Verifica el estado de salud de la API:
```json
{
  "status": "UP",
  "message": "Letras Vivas Book API is running",
  "timestamp": "2024-01-15T10:30:00"
}
```

## 🚨 **Consideraciones de Seguridad**

### **Desarrollo**
- Documentación completa disponible
- Swagger UI accesible públicamente
- Endpoints de prueba habilitados

### **Producción**
- Swagger UI deshabilitado
- Documentación no expuesta
- Solo endpoints de negocio disponibles

## 📚 **Recursos Adicionales**

### **Documentación Oficial**
- [SpringDoc OpenAPI](https://springdoc.org/)
- [OpenAPI Specification](https://swagger.io/specification/)
- [Swagger UI](https://swagger.io/tools/swagger-ui/)

### **Mejores Prácticas**
- ✅ Usar descripciones claras y en español
- ✅ Proporcionar ejemplos de datos
- ✅ Documentar todos los códigos de respuesta
- ✅ Agrupar operaciones por tags lógicos
- ✅ Validar esquemas con anotaciones

## 🔄 **Mantenimiento y Actualización**

### **Actualización de Dependencias**
```bash
# Verificar versiones disponibles
mvn versions:display-dependency-updates

# Actualizar a la última versión estable
mvn versions:use-latest-releases
```

### **Regeneración de Documentación**
La documentación se regenera automáticamente al:
- Reiniciar la aplicación
- Modificar anotaciones OpenAPI
- Cambiar la configuración

---

**Nota**: Esta documentación se actualiza automáticamente con los cambios en el código. Para más información, consulta los comentarios en el código fuente.
