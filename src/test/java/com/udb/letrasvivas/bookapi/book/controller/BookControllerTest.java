package com.udb.letrasvivas.bookapi.book.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.udb.letrasvivas.bookapi.book.dto.BookDto;
import com.udb.letrasvivas.bookapi.book.exception.BookNotFoundException;
import com.udb.letrasvivas.bookapi.book.exception.DuplicateBookException;
import com.udb.letrasvivas.bookapi.book.model.Book;
import com.udb.letrasvivas.bookapi.book.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllBooks_WithPagination_ShouldReturnPaginatedBooks() throws Exception {
        // Given
        List<Book> books = Arrays.asList(
                createTestBook(1L, "Test Book 1", "Author 1", 2020),
                createTestBook(2L, "Test Book 2", "Author 2", 2021)
        );
        Page<Book> bookPage = new PageImpl<>(books, PageRequest.of(0, 10), 2);
        when(bookService.getAllBooks(any(Pageable.class))).thenReturn(bookPage);

        // When & Then
        mockMvc.perform(get("/api/books")
                .param("page", "0")
                .param("size", "10")
                .param("sortBy", "title")
                .param("sortDir", "asc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.content[0].title").value("Test Book 1"))
                .andExpect(jsonPath("$.content[1].title").value("Test Book 2"));
    }

    @Test
    void getBookById_WhenBookExists_ShouldReturnBook() throws Exception {
        // Given
        Book book = createTestBook(1L, "Test Book", "Test Author", 2020);
        when(bookService.getBookById(1L)).thenReturn(book);

        // When & Then
        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Book"))
                .andExpect(jsonPath("$.author").value("Test Author"))
                .andExpect(jsonPath("$.publicationYear").value(2020));
    }

    @Test
    void getBookById_WhenBookDoesNotExist_ShouldReturnNotFound() throws Exception {
        // Given
        when(bookService.getBookById(1L)).thenThrow(new BookNotFoundException(1L));

        // When & Then
        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Book with id 1 not found"));
    }

    @Test
    void searchBooksAdvanced_WithMultipleCriteria_ShouldReturnFilteredResults() throws Exception {
        // Given
        List<Book> books = Arrays.asList(
                createTestBook(1L, "Fiction Book", "Author 1", 2020)
        );
        Page<Book> bookPage = new PageImpl<>(books, PageRequest.of(0, 10), 1);
        when(bookService.searchBooksAdvanced(anyString(), anyString(), anyString(),
                anyInt(), anyInt(), any(BigDecimal.class), any(BigDecimal.class),
                anyBoolean(), any(Pageable.class))).thenReturn(bookPage);

        // When & Then
        mockMvc.perform(get("/api/books/search")
                .param("title", "Fiction")
                .param("author", "Author")
                .param("genre", "Fiction")
                .param("minYear", "2019")
                .param("maxYear", "2021")
                .param("minPrice", "10.00")
                .param("maxPrice", "50.00")
                .param("isAvailable", "true")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].title").value("Fiction Book"));
    }

    @Test
    void createBook_WithValidData_ShouldReturnCreatedBook() throws Exception {
        // Given
        BookDto bookDto = new BookDto("New Book", "New Author", 2023,
                "A great book", "Fiction", 300, 29.99);
        Book createdBook = createTestBook(1L, "New Book", "New Author", 2023);
        when(bookService.createBook(any(BookDto.class))).thenReturn(createdBook);

        // When & Then
        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("New Book"))
                .andExpect(jsonPath("$.author").value("New Author"))
                .andExpect(jsonPath("$.publicationYear").value(2023));
    }

    @Test
    void createBook_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        // Given
        BookDto bookDto = new BookDto("", "", 0, null, null, null, null);

        // When & Then
        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation Error"));
    }

    @Test
    void createBook_WithDuplicateBook_ShouldReturnConflict() throws Exception {
        // Given
        BookDto bookDto = new BookDto("Duplicate Book", "Duplicate Author", 2023,
                "A duplicate book", "Fiction", 300, 29.99);
        when(bookService.createBook(any(BookDto.class)))
                .thenThrow(new DuplicateBookException("Duplicate Book", "Duplicate Author"));

        // When & Then
        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").value("Book with title 'Duplicate Book' by author 'Duplicate Author' already exists"));
    }

    @Test
    void updateBook_WithValidData_ShouldReturnUpdatedBook() throws Exception {
        // Given
        BookDto bookDto = new BookDto("Updated Book", "Updated Author", 2023,
                "An updated book", "Fiction", 350, 34.99);
        Book updatedBook = createTestBook(1L, "Updated Book", "Updated Author", 2023);
        when(bookService.updateBook(anyLong(), any(BookDto.class))).thenReturn(updatedBook);

        // When & Then
        mockMvc.perform(put("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Updated Book"))
                .andExpect(jsonPath("$.author").value("Updated Author"))
                .andExpect(jsonPath("$.publicationYear").value(2023));
    }

    @Test
    void updateBook_WhenBookDoesNotExist_ShouldReturnNotFound() throws Exception {
        // Given
        BookDto bookDto = new BookDto("Updated Book", "Updated Author", 2023,
                "An updated book", "Fiction", 350, 34.99);
        when(bookService.updateBook(anyLong(), any(BookDto.class)))
                .thenThrow(new BookNotFoundException(1L));

        // When & Then
        mockMvc.perform(put("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    void toggleBookAvailability_ShouldReturnUpdatedBook() throws Exception {
        // Given
        Book book = createTestBook(1L, "Test Book", "Test Author", 2020);
        book.setIsAvailable(false);
        when(bookService.toggleBookAvailability(1L)).thenReturn(book);

        // When & Then
        mockMvc.perform(patch("/api/books/1/availability"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.isAvailable").value(false));
    }

    @Test
    void deleteBook_WhenBookExists_ShouldReturnNoContent() throws Exception {
        // Given
        // The service method now returns void, so we don't need to mock a return value
        // Just ensure no exception is thrown

        // When & Then
        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteBook_WhenBookDoesNotExist_ShouldReturnNotFound() throws Exception {
        // Given
        doThrow(new BookNotFoundException(1L)).when(bookService).deleteBook(1L);

        // When & Then
        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    void getBookStatistics_ShouldReturnStatistics() throws Exception {
        // Given
        BookService.BookStatistics statistics = BookService.BookStatistics.builder()
                .totalBooks(100)
                .availableBooks(85)
                .averagePrice(new BigDecimal("25.50"))
                .oldestPublicationYear(1605)
                .newestPublicationYear(2024)
                .build();
        when(bookService.getBookStatistics()).thenReturn(statistics);

        // When & Then
        mockMvc.perform(get("/api/books/statistics"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalBooks").value(100))
                .andExpect(jsonPath("$.availableBooks").value(85))
                .andExpect(jsonPath("$.averagePrice").value(25.50))
                .andExpect(jsonPath("$.oldestPublicationYear").value(1605))
                .andExpect(jsonPath("$.newestPublicationYear").value(2024));
    }

    private Book createTestBook(Long id, String title, String author, Integer publicationYear) {
        Book book = new Book();
        book.setId(id);
        book.setTitle(title);
        book.setAuthor(author);
        book.setPublicationYear(publicationYear);
        book.setDescription("Test description");
        book.setGenre("Fiction");
        book.setPageCount(300);
        book.setPrice(new BigDecimal("29.99"));
        book.setIsAvailable(true);
        book.setCreatedAt(LocalDateTime.now());
        book.setUpdatedAt(LocalDateTime.now());
        book.setVersion(1L);
        return book;
    }
}
