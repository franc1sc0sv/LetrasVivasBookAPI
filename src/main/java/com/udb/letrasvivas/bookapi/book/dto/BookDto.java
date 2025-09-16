package com.udb.letrasvivas.bookapi.book.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data Transfer Object for creating or updating a book")
public class BookDto {

    @NotBlank(message = "Title is required and cannot be empty")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    @Pattern(regexp = "^[a-zA-Z0-9\\s\\-.,:;!?'\"()]+$", message = "Title contains invalid characters")
    @Schema(description = "Book title", example = "Don Quixote", required = true, maxLength = 255)
    private String title;

    @NotBlank(message = "Author is required and cannot be empty")
    @Size(min = 2, max = 255, message = "Author name must be between 2 and 255 characters")
    @Pattern(regexp = "^[a-zA-Z\\s\\-.,]+$", message = "Author name contains invalid characters")
    @Schema(description = "Book author", example = "Miguel de Cervantes", required = true, maxLength = 255)
    private String author;

    @NotNull(message = "Publication year is required")
    @Min(value = 1000, message = "Publication year must be at least 1000")
    @Max(value = 2024, message = "Publication year cannot be in the future")
    @Schema(description = "Year the book was published", example = "1605", required = true, minimum = "1000", maximum = "2024")
    private Integer publicationYear;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    @Schema(description = "Optional book description", example = "A classic Spanish novel about the adventures of Don Quixote", maxLength = 1000)
    private String description;

    @Size(max = 50, message = "Genre cannot exceed 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\s\\-]+$", message = "Genre contains invalid characters")
    @Schema(description = "Book genre", example = "Fiction", maxLength = 50)
    private String genre;

    @Min(value = 1, message = "Page count must be at least 1")
    @Max(value = 10000, message = "Page count cannot exceed 10000")
    @Schema(description = "Number of pages in the book", example = "863", minimum = "1", maximum = "10000")
    private Integer pageCount;

    @DecimalMin(value = "0.0", message = "Price cannot be negative")
    @DecimalMax(value = "9999.99", message = "Price cannot exceed 9999.99")
    @Schema(description = "Book price in USD", example = "29.99", minimum = "0.0", maximum = "9999.99")
    private Double price;
}
