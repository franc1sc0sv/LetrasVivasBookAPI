package com.udb.letrasvivas.bookapi.book.controller;

import com.udb.letrasvivas.bookapi.book.dto.BookDto;
import com.udb.letrasvivas.bookapi.book.model.Book;
import com.udb.letrasvivas.bookapi.book.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
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
@Tag(name = "Book Management", description = "APIs for managing books in Letras Vivas")
public class BookController {

    private final BookService bookService;

    /**
     * Get all books
     */
    @GetMapping
    @Operation(summary = "Get all books", description = "Retrieve a list of all books in the catalog")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved books",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Book.class)))
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
    @Operation(summary = "Get book by ID", description = "Retrieve a specific book by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Book found",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Book.class))),
        @ApiResponse(responseCode = "404", description = "Book not found")
    })
    public ResponseEntity<Book> getBookById(
            @Parameter(description = "ID of the book to retrieve")
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
    @Operation(summary = "Search books by title", description = "Search for books containing the specified title")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search completed successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Book.class)))
    })
    public ResponseEntity<List<Book>> searchBooksByTitle(
            @Parameter(description = "Title to search for")
            @RequestParam String title) {
        log.info("GET /api/books/search?title={} - Searching books by title", title);
        List<Book> books = bookService.searchBooksByTitle(title);
        return ResponseEntity.ok(books);
    }

    /**
     * Create a new book
     */
    @PostMapping
    @Operation(summary = "Create a new book", description = "Add a new book to the catalog")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Book created successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Book.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<Book> createBook(
            @Parameter(description = "Book data to create")
            @Valid @RequestBody BookDto bookDto) {
        log.info("POST /api/books - Creating new book: {}", bookDto.getTitle());
        Book createdBook = bookService.createBook(bookDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    }

    /**
     * Update an existing book
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update a book", description = "Update an existing book by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Book updated successfully",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Book.class))),
        @ApiResponse(responseCode = "404", description = "Book not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<Book> updateBook(
            @Parameter(description = "ID of the book to update")
            @PathVariable Long id,
            @Parameter(description = "Updated book data")
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
    @Operation(summary = "Delete a book", description = "Delete a book by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Book deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Book not found")
    })
    public ResponseEntity<Void> deleteBook(
            @Parameter(description = "ID of the book to delete")
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
