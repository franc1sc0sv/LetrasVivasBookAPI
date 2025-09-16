package com.udb.letrasvivas.bookapi.book.service;

import com.udb.letrasvivas.bookapi.book.dto.BookDto;
import com.udb.letrasvivas.bookapi.book.exception.BookNotFoundException;
import com.udb.letrasvivas.bookapi.book.exception.DuplicateBookException;
import com.udb.letrasvivas.bookapi.book.model.Book;
import com.udb.letrasvivas.bookapi.book.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book testBook;
    private BookDto testBookDto;

    @BeforeEach
    void setUp() {
        testBook = createTestBook(1L, "Test Book", "Test Author", 2020);
        testBookDto = createTestBookDto("Test Book", "Test Author", 2020);
    }

    @Test
    void getAllBooks_WithPagination_ShouldReturnPaginatedBooks() {
        // Given
        List<Book> books = Arrays.asList(testBook);
        Page<Book> bookPage = new PageImpl<>(books, PageRequest.of(0, 10), 1);
        when(bookRepository.findAll(any(Pageable.class))).thenReturn(bookPage);

        // When
        Page<Book> result = bookService.getAllBooks(PageRequest.of(0, 10));

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("Test Book");
        verify(bookRepository).findAll(any(Pageable.class));
    }

    @Test
    void getAllBooks_Legacy_ShouldReturnAllBooks() {
        // Given
        List<Book> books = Arrays.asList(testBook);
        when(bookRepository.findAll()).thenReturn(books);

        // When
        List<Book> result = bookService.getAllBooks();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Test Book");
        verify(bookRepository).findAll();
    }

    @Test
    void getBookById_WhenBookExists_ShouldReturnBook() {
        // Given
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));

        // When
        Book result = bookService.getBookById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Test Book");
        assertThat(result.getAuthor()).isEqualTo("Test Author");
        verify(bookRepository).findById(1L);
    }

    @Test
    void getBookById_WhenBookDoesNotExist_ShouldThrowException() {
        // Given
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> bookService.getBookById(1L))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessage("Book with id 1 not found");
        verify(bookRepository).findById(1L);
    }

    @Test
    void searchBooksAdvanced_WithMultipleCriteria_ShouldReturnFilteredResults() {
        // Given
        List<Book> books = Arrays.asList(testBook);
        Page<Book> bookPage = new PageImpl<>(books, PageRequest.of(0, 10), 1);
        when(bookRepository.findBooksWithAdvancedSearch(
                anyString(), anyString(), anyString(), anyInt(), anyInt(),
                any(BigDecimal.class), any(BigDecimal.class), anyBoolean(), any(Pageable.class)))
                .thenReturn(bookPage);

        // When
        Page<Book> result = bookService.searchBooksAdvanced(
                "Test", "Author", "Fiction", 2019, 2021,
                BigDecimal.valueOf(10.0), BigDecimal.valueOf(50.0), true,
                PageRequest.of(0, 10));

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(bookRepository).findBooksWithAdvancedSearch(
                eq("Test"), eq("Author"), eq("Fiction"), eq(2019), eq(2021),
                eq(BigDecimal.valueOf(10.0)), eq(BigDecimal.valueOf(50.0)), eq(true), any(Pageable.class));
    }

    @Test
    void createBook_WithValidData_ShouldReturnCreatedBook() {
        // Given
        when(bookRepository.existsByTitleAndAuthorIgnoreCase("Test Book", "Test Author"))
                .thenReturn(false);
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        // When
        Book result = bookService.createBook(testBookDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Test Book");
        verify(bookRepository).existsByTitleAndAuthorIgnoreCase("Test Book", "Test Author");
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void createBook_WithDuplicateBook_ShouldThrowException() {
        // Given
        when(bookRepository.existsByTitleAndAuthorIgnoreCase("Test Book", "Test Author"))
                .thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> bookService.createBook(testBookDto))
                .isInstanceOf(DuplicateBookException.class)
                .hasMessage("Book with title 'Test Book' by author 'Test Author' already exists");
        verify(bookRepository).existsByTitleAndAuthorIgnoreCase("Test Book", "Test Author");
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void updateBook_WithValidData_ShouldReturnUpdatedBook() {
        // Given
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(bookRepository.existsByTitleAndAuthorIgnoreCaseExcludingId("Updated Book", "Updated Author", 1L))
                .thenReturn(false);
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        BookDto updateDto = createTestBookDto("Updated Book", "Updated Author", 2021);

        // When
        Book result = bookService.updateBook(1L, updateDto);

        // Then
        assertThat(result).isNotNull();
        verify(bookRepository).findById(1L);
        verify(bookRepository).existsByTitleAndAuthorIgnoreCaseExcludingId("Updated Book", "Updated Author", 1L);
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void updateBook_WhenBookDoesNotExist_ShouldThrowException() {
        // Given
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> bookService.updateBook(1L, testBookDto))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessage("Book with id 1 not found");
        verify(bookRepository).findById(1L);
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void updateBook_WithDuplicateBook_ShouldThrowException() {
        // Given
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(bookRepository.existsByTitleAndAuthorIgnoreCaseExcludingId("Test Book", "Test Author", 1L))
                .thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> bookService.updateBook(1L, testBookDto))
                .isInstanceOf(DuplicateBookException.class)
                .hasMessage("Book with title 'Test Book' by author 'Test Author' already exists");
        verify(bookRepository).findById(1L);
        verify(bookRepository).existsByTitleAndAuthorIgnoreCaseExcludingId("Test Book", "Test Author", 1L);
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void deleteBook_WhenBookExists_ShouldDeleteSuccessfully() {
        // Given
        when(bookRepository.existsById(1L)).thenReturn(true);

        // When
        bookService.deleteBook(1L);

        // Then
        verify(bookRepository).existsById(1L);
        verify(bookRepository).deleteById(1L);
    }

    @Test
    void deleteBook_WhenBookDoesNotExist_ShouldThrowException() {
        // Given
        when(bookRepository.existsById(1L)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> bookService.deleteBook(1L))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessage("Book with id 1 not found");
        verify(bookRepository).existsById(1L);
        verify(bookRepository, never()).deleteById(anyLong());
    }

    @Test
    void toggleBookAvailability_WhenBookExists_ShouldToggleAvailability() {
        // Given
        testBook.setIsAvailable(true);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        // When
        Book result = bookService.toggleBookAvailability(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getIsAvailable()).isFalse();
        verify(bookRepository).findById(1L);
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void toggleBookAvailability_WhenBookDoesNotExist_ShouldThrowException() {
        // Given
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> bookService.toggleBookAvailability(1L))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessage("Book with id 1 not found");
        verify(bookRepository).findById(1L);
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void bookExists_WhenBookExists_ShouldReturnTrue() {
        // Given
        when(bookRepository.existsById(1L)).thenReturn(true);

        // When
        boolean result = bookService.bookExists(1L);

        // Then
        assertThat(result).isTrue();
        verify(bookRepository).existsById(1L);
    }

    @Test
    void bookExists_WhenBookDoesNotExist_ShouldReturnFalse() {
        // Given
        when(bookRepository.existsById(1L)).thenReturn(false);

        // When
        boolean result = bookService.bookExists(1L);

        // Then
        assertThat(result).isFalse();
        verify(bookRepository).existsById(1L);
    }

    @Test
    void getBookStatistics_ShouldReturnStatistics() {
        // Given
        when(bookRepository.countAllBooks()).thenReturn(100L);
        when(bookRepository.countAvailableBooks()).thenReturn(85L);
        when(bookRepository.getAveragePrice()).thenReturn(BigDecimal.valueOf(25.50));
        when(bookRepository.getOldestPublicationYear()).thenReturn(1605);
        when(bookRepository.getNewestPublicationYear()).thenReturn(2024);

        // When
        BookService.BookStatistics result = bookService.getBookStatistics();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTotalBooks()).isEqualTo(100);
        assertThat(result.getAvailableBooks()).isEqualTo(85);
        assertThat(result.getAveragePrice()).isEqualTo(BigDecimal.valueOf(25.50));
        assertThat(result.getOldestPublicationYear()).isEqualTo(1605);
        assertThat(result.getNewestPublicationYear()).isEqualTo(2024);
        verify(bookRepository).countAllBooks();
        verify(bookRepository).countAvailableBooks();
        verify(bookRepository).getAveragePrice();
        verify(bookRepository).getOldestPublicationYear();
        verify(bookRepository).getNewestPublicationYear();
    }

    @Test
    void searchBooksByTitle_WithPagination_ShouldReturnFilteredResults() {
        // Given
        List<Book> books = Arrays.asList(testBook);
        Page<Book> bookPage = new PageImpl<>(books, PageRequest.of(0, 10), 1);
        when(bookRepository.findByTitleContainingIgnoreCase("Test", PageRequest.of(0, 10)))
                .thenReturn(bookPage);

        // When
        Page<Book> result = bookService.searchBooksByTitle("Test", PageRequest.of(0, 10));

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(bookRepository).findByTitleContainingIgnoreCase("Test", PageRequest.of(0, 10));
    }

    @Test
    void searchBooksByAuthor_WithPagination_ShouldReturnFilteredResults() {
        // Given
        List<Book> books = Arrays.asList(testBook);
        Page<Book> bookPage = new PageImpl<>(books, PageRequest.of(0, 10), 1);
        when(bookRepository.findByAuthorContainingIgnoreCase("Author", PageRequest.of(0, 10)))
                .thenReturn(bookPage);

        // When
        Page<Book> result = bookService.searchBooksByAuthor("Author", PageRequest.of(0, 10));

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(bookRepository).findByAuthorContainingIgnoreCase("Author", PageRequest.of(0, 10));
    }

    @Test
    void searchBooksByGenre_WithPagination_ShouldReturnFilteredResults() {
        // Given
        List<Book> books = Arrays.asList(testBook);
        Page<Book> bookPage = new PageImpl<>(books, PageRequest.of(0, 10), 1);
        when(bookRepository.findByGenreIgnoreCase("Fiction", PageRequest.of(0, 10)))
                .thenReturn(bookPage);

        // When
        Page<Book> result = bookService.searchBooksByGenre("Fiction", PageRequest.of(0, 10));

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(bookRepository).findByGenreIgnoreCase("Fiction", PageRequest.of(0, 10));
    }

    @Test
    void searchBooksByPublicationYear_WithPagination_ShouldReturnFilteredResults() {
        // Given
        List<Book> books = Arrays.asList(testBook);
        Page<Book> bookPage = new PageImpl<>(books, PageRequest.of(0, 10), 1);
        when(bookRepository.findByPublicationYear(2020, PageRequest.of(0, 10)))
                .thenReturn(bookPage);

        // When
        Page<Book> result = bookService.searchBooksByPublicationYear(2020, PageRequest.of(0, 10));

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(bookRepository).findByPublicationYear(2020, PageRequest.of(0, 10));
    }

    @Test
    void searchBooksByPublicationYearRange_WithPagination_ShouldReturnFilteredResults() {
        // Given
        List<Book> books = Arrays.asList(testBook);
        Page<Book> bookPage = new PageImpl<>(books, PageRequest.of(0, 10), 1);
        when(bookRepository.findByPublicationYearBetween(2019, 2021, PageRequest.of(0, 10)))
                .thenReturn(bookPage);

        // When
        Page<Book> result = bookService.searchBooksByPublicationYearRange(2019, 2021, PageRequest.of(0, 10));

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(bookRepository).findByPublicationYearBetween(2019, 2021, PageRequest.of(0, 10));
    }

    @Test
    void searchBooksByPriceRange_WithPagination_ShouldReturnFilteredResults() {
        // Given
        List<Book> books = Arrays.asList(testBook);
        Page<Book> bookPage = new PageImpl<>(books, PageRequest.of(0, 10), 1);
        when(bookRepository.findByPriceBetween(BigDecimal.valueOf(10.0), BigDecimal.valueOf(50.0), PageRequest.of(0, 10)))
                .thenReturn(bookPage);

        // When
        Page<Book> result = bookService.searchBooksByPriceRange(BigDecimal.valueOf(10.0), BigDecimal.valueOf(50.0), PageRequest.of(0, 10));

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(bookRepository).findByPriceBetween(BigDecimal.valueOf(10.0), BigDecimal.valueOf(50.0), PageRequest.of(0, 10));
    }

    @Test
    void searchBooksByAvailability_WithPagination_ShouldReturnFilteredResults() {
        // Given
        List<Book> books = Arrays.asList(testBook);
        Page<Book> bookPage = new PageImpl<>(books, PageRequest.of(0, 10), 1);
        when(bookRepository.findByIsAvailable(true, PageRequest.of(0, 10)))
                .thenReturn(bookPage);

        // When
        Page<Book> result = bookService.searchBooksByAvailability(true, PageRequest.of(0, 10));

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(bookRepository).findByIsAvailable(true, PageRequest.of(0, 10));
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
        book.setPrice(BigDecimal.valueOf(29.99));
        book.setIsAvailable(true);
        book.setCreatedAt(LocalDateTime.now());
        book.setUpdatedAt(LocalDateTime.now());
        book.setVersion(1L);
        return book;
    }

    private BookDto createTestBookDto(String title, String author, Integer publicationYear) {
        BookDto dto = new BookDto();
        dto.setTitle(title);
        dto.setAuthor(author);
        dto.setPublicationYear(publicationYear);
        dto.setDescription("Test description");
        dto.setGenre("Fiction");
        dto.setPageCount(300);
        dto.setPrice(29.99);
        return dto;
    }
}
