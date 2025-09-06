package com.udb.letrasvivas.bookapi.book.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa un libro en el sistema")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del libro", example = "1")
    private Long id;

    @Column(nullable = false, length = 255)
    @Schema(description = "Título del libro", example = "Don Quijote de la Mancha", required = true)
    private String title;

    @Column(nullable = false, length = 255)
    @Schema(description = "Autor del libro", example = "Miguel de Cervantes", required = true)
    private String author;

    @Column(name = "publication_year", nullable = false)
    @Schema(description = "Año de publicación del libro", example = "1605", required = true)
    private int publicationYear;
}
