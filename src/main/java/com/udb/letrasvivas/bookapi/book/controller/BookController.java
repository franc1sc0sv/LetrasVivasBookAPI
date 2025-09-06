package com.udb.letrasvivas.bookapi.book.controller;

import com.udb.letrasvivas.bookapi.book.dto.BookDto;
import com.udb.letrasvivas.bookapi.book.model.Book;
import com.udb.letrasvivas.bookapi.book.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Book Management", description = "APIs para la gestión de libros en Letras Vivas")
public class BookController {

    private final BookService bookService;

    /**
     * Get all books
     */
    @GetMapping
    @Operation(
            summary = "Obtener todos los libros",
            description = "Recupera una lista de todos los libros en el catálogo",
            operationId = "getAllBooks"
    )
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Libros recuperados exitosamente",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = Book.class),
                        examples = @ExampleObject(
                                name = "Lista de libros",
                                value = "[{\"id\": 1, \"title\": \"Don Quijote\", \"author\": \"Cervantes\", \"publicationYear\": 1605}]"
                        )
                )
        )
    })
    public ResponseEntity<List<Book>> getAllBooks() {
        log.info("GET /api/books - Fetching all books");
        List<Book> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    /**
     * Get book by ID
     */
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
                        schema = @Schema(implementation = Book.class),
                        examples = @ExampleObject(
                                name = "Libro encontrado",
                                value = "{\"id\": 1, \"title\": \"Don Quijote\", \"author\": \"Cervantes\", \"publicationYear\": 1605}"
                        )
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Libro no encontrado",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "Error 404",
                                value = "{\"message\": \"Book not found\", \"status\": 404}"
                        )
                )
        )
    })
    public ResponseEntity<Book> getBookById(
            @Parameter(description = "ID del libro a recuperar", example = "1")
            @PathVariable Long id) {
        log.info("GET /api/books/{} - Fetching book by ID", id);
        return bookService.getBookById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Search books by title
     */
    @GetMapping("/search")
    @Operation(
            summary = "Buscar libros por título",
            description = "Busca libros que contengan el título especificado",
            operationId = "searchBooksByTitle"
    )
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Búsqueda completada exitosamente",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = Book.class),
                        examples = @ExampleObject(
                                name = "Resultados de búsqueda",
                                value = "[{\"id\": 1, \"title\": \"Don Quijote\", \"author\": \"Cervantes\", \"publicationYear\": 1605}]"
                        )
                )
        )
    })
    public ResponseEntity<List<Book>> searchBooksByTitle(
            @Parameter(description = "Título a buscar", example = "Quijote")
            @RequestParam String title) {
        log.info("GET /api/books/search?title={} - Searching books by title", title);
        List<Book> books = bookService.searchBooksByTitle(title);
        return ResponseEntity.ok(books);
    }

    /**
     * Create a new book
     */
    @PostMapping
    @Operation(
            summary = "Crear un nuevo libro",
            description = "Agrega un nuevo libro al catálogo",
            operationId = "createBook"
    )
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "201",
                description = "Libro creado exitosamente",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = Book.class),
                        examples = @ExampleObject(
                                name = "Libro creado",
                                value = "{\"id\": 1, \"title\": \"Nuevo Libro\", \"author\": \"Nuevo Autor\", \"publicationYear\": 2024}"
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Datos de entrada inválidos",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "Error de validación",
                                value = "{\"message\": \"Validation failed\", \"errors\": [\"Title is required\"]}"
                        )
                )
        )
    })
    public ResponseEntity<Book> createBook(
            @Parameter(
                    description = "Datos del libro a crear",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = BookDto.class),
                            examples = @ExampleObject(
                                    name = "Ejemplo de creación",
                                    value = "{\"title\": \"Nuevo Libro\", \"author\": \"Nuevo Autor\", \"publicationYear\": 2024}"
                            )
                    )
            )
            @Valid @RequestBody BookDto bookDto) {
        log.info("POST /api/books - Creating new book: {}", bookDto.getTitle());
        Book createdBook = bookService.createBook(bookDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    }

    /**
     * Update an existing book
     */
    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar un libro",
            description = "Actualiza un libro existente por ID",
            operationId = "updateBook"
    )
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Libro actualizado exitosamente",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = Book.class),
                        examples = @ExampleObject(
                                name = "Libro actualizado",
                                value = "{\"id\": 1, \"title\": \"Libro Actualizado\", \"author\": \"Autor Actualizado\", \"publicationYear\": 2024}"
                        )
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Libro no encontrado",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "Error 404",
                                value = "{\"message\": \"Book not found\", \"status\": 404}"
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Datos de entrada inválidos",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "Error de validación",
                                value = "{\"message\": \"Validation failed\", \"errors\": [\"Title is required\"]}"
                        )
                )
        )
    })
    public ResponseEntity<Book> updateBook(
            @Parameter(description = "ID del libro a actualizar", example = "1")
            @PathVariable Long id,
            @Parameter(
                    description = "Datos actualizados del libro",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = BookDto.class),
                            examples = @ExampleObject(
                                    name = "Ejemplo de actualización",
                                    value = "{\"title\": \"Libro Actualizado\", \"author\": \"Autor Actualizado\", \"publicationYear\": 2024}"
                            )
                    )
            )
            @Valid @RequestBody BookDto bookDto) {
        log.info("PUT /api/books/{} - Updating book", id);
        return bookService.updateBook(id, bookDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Delete a book
     */
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar un libro",
            description = "Elimina un libro por ID",
            operationId = "deleteBook"
    )
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "204",
                description = "Libro eliminado exitosamente"
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Libro no encontrado",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "Error 404",
                                value = "{\"message\": \"Book not found\", \"status\": 404}"
                        )
                )
        )
    })
    public ResponseEntity<Void> deleteBook(
            @Parameter(description = "ID del libro a eliminar", example = "1")
            @PathVariable Long id) {
        log.info("DELETE /api/books/{} - Deleting book", id);
        boolean deleted = bookService.deleteBook(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
