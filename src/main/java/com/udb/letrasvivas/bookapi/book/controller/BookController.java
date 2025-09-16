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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Book Management", description = "APIs for managing books in Letras Vivas")
public class BookController {

    private final BookService bookService;

    /**
     * Get all books with pagination
     */
    @GetMapping
    @Operation(
            summary = "Get all books",
            description = "Retrieve a paginated list of all books in the catalog",
            operationId = "getAllBooks"
    )
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Books retrieved successfully",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = Page.class),
                        examples = @ExampleObject(
                                name = "Paginated books",
                                value = "{\"content\": [{\"id\": 1, \"title\": \"Don Quixote\", \"author\": \"Miguel de Cervantes\", \"publicationYear\": 1605}], \"totalElements\": 1, \"totalPages\": 1}"
                        )
                )
        )
    })
    public ResponseEntity<Page<Book>> getAllBooks(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field", example = "title")
            @RequestParam(defaultValue = "title") String sortBy,
            @Parameter(description = "Sort direction", example = "asc")
            @RequestParam(defaultValue = "asc") String sortDir) {

        log.info("GET /api/books - Fetching all books with pagination: page={}, size={}, sortBy={}, sortDir={}",
                page, size, sortBy, sortDir);

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Book> books = bookService.getAllBooks(pageable);
        return ResponseEntity.ok(books);
    }

    /**
     * Get book by ID
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Get book by ID",
            description = "Retrieve a specific book by its ID",
            operationId = "getBookById"
    )
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Book found",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = Book.class),
                        examples = @ExampleObject(
                                name = "Book found",
                                value = "{\"id\": 1, \"title\": \"Don Quixote\", \"author\": \"Miguel de Cervantes\", \"publicationYear\": 1605}"
                        )
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Book not found",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "Error 404",
                                value = "{\"message\": \"Book with id 1 not found\", \"status\": 404}"
                        )
                )
        )
    })
    public ResponseEntity<Book> getBookById(
            @Parameter(description = "ID of the book to retrieve", example = "1")
            @PathVariable Long id) {
        log.info("GET /api/books/{} - Fetching book by ID", id);
        Book book = bookService.getBookById(id);
        return ResponseEntity.ok(book);
    }

    /**
     * Advanced search for books
     */
    @GetMapping("/search")
    @Operation(
            summary = "Advanced book search",
            description = "Search books using multiple criteria with pagination",
            operationId = "searchBooksAdvanced"
    )
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Search completed successfully",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = Page.class),
                        examples = @ExampleObject(
                                name = "Search results",
                                value = "{\"content\": [{\"id\": 1, \"title\": \"Don Quixote\", \"author\": \"Miguel de Cervantes\"}], \"totalElements\": 1}"
                        )
                )
        )
    })
    public ResponseEntity<Page<Book>> searchBooksAdvanced(
            @Parameter(description = "Title to search for", example = "Quixote")
            @RequestParam(required = false) String title,
            @Parameter(description = "Author to search for", example = "Cervantes")
            @RequestParam(required = false) String author,
            @Parameter(description = "Genre to filter by", example = "Fiction")
            @RequestParam(required = false) String genre,
            @Parameter(description = "Minimum publication year", example = "1600")
            @RequestParam(required = false) Integer minYear,
            @Parameter(description = "Maximum publication year", example = "1700")
            @RequestParam(required = false) Integer maxYear,
            @Parameter(description = "Minimum price", example = "10.00")
            @RequestParam(required = false) BigDecimal minPrice,
            @Parameter(description = "Maximum price", example = "50.00")
            @RequestParam(required = false) BigDecimal maxPrice,
            @Parameter(description = "Availability status", example = "true")
            @RequestParam(required = false) Boolean isAvailable,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field", example = "title")
            @RequestParam(defaultValue = "title") String sortBy,
            @Parameter(description = "Sort direction", example = "asc")
            @RequestParam(defaultValue = "asc") String sortDir) {

        log.info("GET /api/books/search - Advanced search with criteria: title={}, author={}, genre={}, "
                + "minYear={}, maxYear={}, minPrice={}, maxPrice={}, isAvailable={}, page={}, size={}, sortBy={}, sortDir={}",
                title, author, genre, minYear, maxYear, minPrice, maxPrice, isAvailable, page, size, sortBy, sortDir);

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Book> books = bookService.searchBooksAdvanced(
                title, author, genre, minYear, maxYear, minPrice, maxPrice, isAvailable, pageable);
        return ResponseEntity.ok(books);
    }

    /**
     * Search books by title (legacy endpoint)
     */
    @GetMapping("/search/title")
    @Operation(
            summary = "Search books by title",
            description = "Search books that contain the specified title",
            operationId = "searchBooksByTitle"
    )
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Search completed successfully",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = List.class),
                        examples = @ExampleObject(
                                name = "Search results",
                                value = "[{\"id\": 1, \"title\": \"Don Quixote\", \"author\": \"Miguel de Cervantes\", \"publicationYear\": 1605}]"
                        )
                )
        )
    })
    public ResponseEntity<List<Book>> searchBooksByTitle(
            @Parameter(description = "Title to search for", example = "Quixote")
            @RequestParam String title) {
        log.info("GET /api/books/search/title?title={} - Searching books by title", title);
        List<Book> books = bookService.searchBooksByTitle(title);
        return ResponseEntity.ok(books);
    }

    /**
     * Search books by author (legacy endpoint)
     */
    @GetMapping("/search/author")
    @Operation(
            summary = "Search books by author",
            description = "Search books that contain the specified author name",
            operationId = "searchBooksByAuthor"
    )
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Search completed successfully",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = List.class),
                        examples = @ExampleObject(
                                name = "Search results",
                                value = "[{\"id\": 1, \"title\": \"Don Quixote\", \"author\": \"Miguel de Cervantes\", \"publicationYear\": 1605}]"
                        )
                )
        )
    })
    public ResponseEntity<List<Book>> searchBooksByAuthor(
            @Parameter(description = "Author to search for", example = "Cervantes")
            @RequestParam String author) {
        log.info("GET /api/books/search/author?author={} - Searching books by author", author);
        List<Book> books = bookService.searchBooksByAuthor(author);
        return ResponseEntity.ok(books);
    }

    /**
     * Get book statistics
     */
    @GetMapping("/statistics")
    @Operation(
            summary = "Get book statistics",
            description = "Retrieve statistics about the book catalog",
            operationId = "getBookStatistics"
    )
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Statistics retrieved successfully",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "Book statistics",
                                value = "{\"totalBooks\": 100, \"availableBooks\": 85, \"averagePrice\": 25.50, \"oldestPublicationYear\": 1605, \"newestPublicationYear\": 2024}"
                        )
                )
        )
    })
    public ResponseEntity<BookService.BookStatistics> getBookStatistics() {
        log.info("GET /api/books/statistics - Fetching book statistics");
        BookService.BookStatistics statistics = bookService.getBookStatistics();
        return ResponseEntity.ok(statistics);
    }

    /**
     * Create a new book
     */
    @PostMapping
    @Operation(
            summary = "Create a new book",
            description = "Add a new book to the catalog",
            operationId = "createBook"
    )
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "201",
                description = "Book created successfully",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = Book.class),
                        examples = @ExampleObject(
                                name = "Book created",
                                value = "{\"id\": 1, \"title\": \"New Book\", \"author\": \"New Author\", \"publicationYear\": 2024}"
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Invalid input data",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "Validation error",
                                value = "{\"message\": \"Validation failed\", \"errors\": [\"Title is required\"]}"
                        )
                )
        ),
        @ApiResponse(
                responseCode = "409",
                description = "Book already exists",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "Conflict error",
                                value = "{\"message\": \"Book with title 'New Book' by author 'New Author' already exists\", \"status\": 409}"
                        )
                )
        )
    })
    public ResponseEntity<Book> createBook(
            @Parameter(
                    description = "Book data to create",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = BookDto.class),
                            examples = @ExampleObject(
                                    name = "Book creation example",
                                    value = "{\"title\": \"New Book\", \"author\": \"New Author\", \"publicationYear\": 2024, \"description\": \"A great book\", \"genre\": \"Fiction\", \"pageCount\": 300, \"price\": 29.99}"
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
            summary = "Update a book",
            description = "Update an existing book by ID",
            operationId = "updateBook"
    )
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Book updated successfully",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = Book.class),
                        examples = @ExampleObject(
                                name = "Book updated",
                                value = "{\"id\": 1, \"title\": \"Updated Book\", \"author\": \"Updated Author\", \"publicationYear\": 2024}"
                        )
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Book not found",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "Error 404",
                                value = "{\"message\": \"Book with id 1 not found\", \"status\": 404}"
                        )
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Invalid input data",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "Validation error",
                                value = "{\"message\": \"Validation failed\", \"errors\": [\"Title is required\"]}"
                        )
                )
        ),
        @ApiResponse(
                responseCode = "409",
                description = "Book already exists",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "Conflict error",
                                value = "{\"message\": \"Book with title 'Updated Book' by author 'Updated Author' already exists\", \"status\": 409}"
                        )
                )
        )
    })
    public ResponseEntity<Book> updateBook(
            @Parameter(description = "ID of the book to update", example = "1")
            @PathVariable Long id,
            @Parameter(
                    description = "Updated book data",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = BookDto.class),
                            examples = @ExampleObject(
                                    name = "Book update example",
                                    value = "{\"title\": \"Updated Book\", \"author\": \"Updated Author\", \"publicationYear\": 2024, \"description\": \"An updated book\", \"genre\": \"Fiction\", \"pageCount\": 350, \"price\": 34.99}"
                            )
                    )
            )
            @Valid @RequestBody BookDto bookDto) {
        log.info("PUT /api/books/{} - Updating book", id);
        Book updatedBook = bookService.updateBook(id, bookDto);
        return ResponseEntity.ok(updatedBook);
    }

    /**
     * Toggle book availability
     */
    @PatchMapping("/{id}/availability")
    @Operation(
            summary = "Toggle book availability",
            description = "Toggle the availability status of a book",
            operationId = "toggleBookAvailability"
    )
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Book availability toggled successfully",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = Book.class),
                        examples = @ExampleObject(
                                name = "Availability toggled",
                                value = "{\"id\": 1, \"title\": \"Don Quixote\", \"author\": \"Miguel de Cervantes\", \"isAvailable\": false}"
                        )
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Book not found",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "Error 404",
                                value = "{\"message\": \"Book with id 1 not found\", \"status\": 404}"
                        )
                )
        )
    })
    public ResponseEntity<Book> toggleBookAvailability(
            @Parameter(description = "ID of the book to toggle availability", example = "1")
            @PathVariable Long id) {
        log.info("PATCH /api/books/{}/availability - Toggling book availability", id);
        Book updatedBook = bookService.toggleBookAvailability(id);
        return ResponseEntity.ok(updatedBook);
    }

    /**
     * Delete a book
     */
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a book",
            description = "Delete a book by ID",
            operationId = "deleteBook"
    )
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "204",
                description = "Book deleted successfully"
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Book not found",
                content = @Content(
                        mediaType = "application/json",
                        examples = @ExampleObject(
                                name = "Error 404",
                                value = "{\"message\": \"Book with id 1 not found\", \"status\": 404}"
                        )
                )
        )
    })
    public ResponseEntity<Void> deleteBook(
            @Parameter(description = "ID of the book to delete", example = "1")
            @PathVariable Long id) {
        log.info("DELETE /api/books/{} - Deleting book", id);
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
