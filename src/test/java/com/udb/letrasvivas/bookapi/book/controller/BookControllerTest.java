package com.udb.letrasvivas.bookapi.book.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.udb.letrasvivas.bookapi.book.dto.BookDto;
import com.udb.letrasvivas.bookapi.book.model.Book;
import com.udb.letrasvivas.bookapi.book.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
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
    void getAllBooks_ShouldReturnBooks() throws Exception {
        // Given
        Book book1 = new Book(1L, "Don Quixote", "Miguel de Cervantes", 1605);
        Book book2 = new Book(2L, "The Great Gatsby", "F. Scott Fitzgerald", 1925);
        when(bookService.getAllBooks()).thenReturn(Arrays.asList(book1, book2));

        // When & Then
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Don Quixote"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("The Great Gatsby"));
    }

    @Test
    void getBookById_WhenBookExists_ShouldReturnBook() throws Exception {
        // Given
        Book book = new Book(1L, "Don Quixote", "Miguel de Cervantes", 1605);
        when(bookService.getBookById(1L)).thenReturn(Optional.of(book));

        // When & Then
        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Don Quixote"));
    }

    @Test
    void getBookById_WhenBookDoesNotExist_ShouldReturn404() throws Exception {
        // Given
        when(bookService.getBookById(999L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/books/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createBook_WithValidData_ShouldReturnCreatedBook() throws Exception {
        // Given
        BookDto bookDto = new BookDto("Don Quixote", "Miguel de Cervantes", 1605);
        Book createdBook = new Book(1L, "Don Quixote", "Miguel de Cervantes", 1605);
        when(bookService.createBook(any(BookDto.class))).thenReturn(createdBook);

        // When & Then
        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Don Quixote"));
    }

    @Test
    void searchBooksByTitle_ShouldReturnMatchingBooks() throws Exception {
        // Given
        Book book = new Book(1L, "Don Quixote", "Miguel de Cervantes", 1605);
        when(bookService.searchBooksByTitle("Quixote")).thenReturn(Arrays.asList(book));

        // When & Then
        mockMvc.perform(get("/api/books/search")
                .param("title", "Quixote"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Don Quixote"));
    }

    @Test
    void deleteBook_WhenBookExists_ShouldReturn204() throws Exception {
        // Given
        when(bookService.deleteBook(1L)).thenReturn(true);

        // When & Then
        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteBook_WhenBookDoesNotExist_ShouldReturn404() throws Exception {
        // Given
        when(bookService.deleteBook(999L)).thenReturn(false);

        // When & Then
        mockMvc.perform(delete("/api/books/999"))
                .andExpect(status().isNotFound());
    }
}
