package com.udb.letrasvivas.bookapi.book.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "books", indexes = {
    @Index(name = "idx_book_title", columnList = "title"),
    @Index(name = "idx_book_author", columnList = "author"),
    @Index(name = "idx_book_publication_year", columnList = "publication_year"),
    @Index(name = "idx_book_genre", columnList = "genre")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entity representing a book in the system")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the book", example = "1")
    private Long id;

    @Column(nullable = false, length = 255)
    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title cannot exceed 255 characters")
    @Schema(description = "Book title", example = "Don Quixote", required = true)
    private String title;

    @Column(nullable = false, length = 255)
    @NotBlank(message = "Author is required")
    @Size(max = 255, message = "Author name cannot exceed 255 characters")
    @Schema(description = "Book author", example = "Miguel de Cervantes", required = true)
    private String author;

    @Column(name = "publication_year", nullable = false)
    @NotNull(message = "Publication year is required")
    @Min(value = 1000, message = "Publication year must be at least 1000")
    @Max(value = 2024, message = "Publication year cannot be in the future")
    @Schema(description = "Year the book was published", example = "1605", required = true)
    private Integer publicationYear;

    @Column(length = 1000)
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    @Schema(description = "Book description", example = "A classic Spanish novel about the adventures of Don Quixote")
    private String description;

    @Column(length = 50)
    @Size(max = 50, message = "Genre cannot exceed 50 characters")
    @Schema(description = "Book genre", example = "Fiction")
    private String genre;

    @Column(name = "page_count")
    @Min(value = 1, message = "Page count must be at least 1")
    @Max(value = 10000, message = "Page count cannot exceed 10000")
    @Schema(description = "Number of pages in the book", example = "863")
    private Integer pageCount;

    @Column(precision = 10, scale = 2)
    @DecimalMin(value = "0.0", message = "Price cannot be negative")
    @DecimalMax(value = "9999.99", message = "Price cannot exceed 9999.99")
    @Schema(description = "Book price in USD", example = "29.99")
    private BigDecimal price;

    @Column(name = "is_available", nullable = false)
    @Schema(description = "Whether the book is available for borrowing", example = "true")
    private Boolean isAvailable = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    @Schema(description = "Timestamp when the book was created")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    @Schema(description = "Timestamp when the book was last updated")
    private LocalDateTime updatedAt;

    @Version
    @Schema(description = "Version number for optimistic locking")
    private Long version;
}
