package com.udb.letrasvivas.bookapi.book.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para crear o actualizar un libro")
public class BookDto {

    @NotBlank(message = "Title is required")
    @Schema(description = "Título del libro", example = "Don Quijote de la Mancha", required = true)
    private String title;

    @NotBlank(message = "Author is required")
    @Schema(description = "Autor del libro", example = "Miguel de Cervantes", required = true)
    private String author;

    @Min(value = 1000, message = "Publication year must be valid")
    @Schema(description = "Año de publicación del libro", example = "1605", required = true, minimum = "1000")
    private int publicationYear;
}
