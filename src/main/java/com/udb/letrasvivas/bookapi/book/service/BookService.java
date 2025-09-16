package com.udb.letrasvivas.bookapi.book.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.udb.letrasvivas.bookapi.book.dto.BookDto;
import com.udb.letrasvivas.bookapi.book.exception.BookNotFoundException;
import com.udb.letrasvivas.bookapi.book.exception.DuplicateBookException;
import com.udb.letrasvivas.bookapi.book.model.Book;
import com.udb.letrasvivas.bookapi.book.repository.BookRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BookService {

    private final BookRepository bookRepository;

    /**
     * Get all books with pagination
     */
    @Transactional(readOnly = true)
    public Page<Book> getAllBooks(Pageable pageable) {
        log.info("Fetching all books with pagination: {}", pageable);
        return bookRepository.findAll(pageable);
    }

    /**
     * Get all books (legacy method for backward compatibility)
     */
    @Transactional(readOnly = true)
    public List<Book> getAllBooks() {
        log.info("Fetching all books");
        return bookRepository.findAll();
    }

    /**
     * Get book by ID
     */
    @Transactional(readOnly = true)
    public Book getBookById(Long id) {
        log.info("Fetching book with id: {}", id);
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    /**
     * Get book by ID (legacy method returning Optional)
     */
    @Transactional(readOnly = true)
    public Optional<Book> getBookByIdOptional(Long id) {
        log.info("Fetching book with id: {}", id);
        return bookRepository.findById(id);
    }

    /**
     * Search books by title with pagination
     */
    @Transactional(readOnly = true)
    public Page<Book> searchBooksByTitle(String title, Pageable pageable) {
        log.info("Searching books with title containing: {} with pagination: {}", title, pageable);
        return bookRepository.findByTitleContainingIgnoreCase(title, pageable);
    }

    /**
     * Search books by title (legacy method)
     */
    @Transactional(readOnly = true)
    public List<Book> searchBooksByTitle(String title) {
        log.info("Searching books with title containing: {}", title);
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    /**
     * Search books by author with pagination
     */
    @Transactional(readOnly = true)
    public Page<Book> searchBooksByAuthor(String author, Pageable pageable) {
        log.info("Searching books with author containing: {} with pagination: {}", author, pageable);
        return bookRepository.findByAuthorContainingIgnoreCase(author, pageable);
    }

    /**
     * Search books by author (legacy method)
     */
    @Transactional(readOnly = true)
    public List<Book> searchBooksByAuthor(String author) {
        log.info("Searching books with author containing: {}", author);
        return bookRepository.findByAuthorContainingIgnoreCase(author);
    }

    /**
     * Advanced search with multiple criteria
     */
    @Transactional(readOnly = true)
    public Page<Book> searchBooksAdvanced(
            String title, String author, String genre,
            Integer minYear, Integer maxYear,
            BigDecimal minPrice, BigDecimal maxPrice,
            Boolean isAvailable, Pageable pageable) {
        log.info("Advanced search with criteria - title: {}, author: {}, genre: {}, "
                + "minYear: {}, maxYear: {}, minPrice: {}, maxPrice: {}, isAvailable: {}, pagination: {}",
                title, author, genre, minYear, maxYear, minPrice, maxPrice, isAvailable, pageable);

        return bookRepository.findBooksWithAdvancedSearch(
                title, author, genre, minYear, maxYear, minPrice, maxPrice, isAvailable, pageable);
    }

    /**
     * Search books by genre with pagination
     */
    @Transactional(readOnly = true)
    public Page<Book> searchBooksByGenre(String genre, Pageable pageable) {
        log.info("Searching books by genre: {} with pagination: {}", genre, pageable);
        return bookRepository.findByGenreIgnoreCase(genre, pageable);
    }

    /**
     * Search books by publication year with pagination
     */
    @Transactional(readOnly = true)
    public Page<Book> searchBooksByPublicationYear(Integer year, Pageable pageable) {
        log.info("Searching books by publication year: {} with pagination: {}", year, pageable);
        return bookRepository.findByPublicationYear(year, pageable);
    }

    /**
     * Search books by publication year range with pagination
     */
    @Transactional(readOnly = true)
    public Page<Book> searchBooksByPublicationYearRange(Integer startYear, Integer endYear, Pageable pageable) {
        log.info("Searching books by publication year range: {} - {} with pagination: {}", startYear, endYear, pageable);
        return bookRepository.findByPublicationYearBetween(startYear, endYear, pageable);
    }

    /**
     * Search books by price range with pagination
     */
    @Transactional(readOnly = true)
    public Page<Book> searchBooksByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        log.info("Searching books by price range: {} - {} with pagination: {}", minPrice, maxPrice, pageable);
        return bookRepository.findByPriceBetween(minPrice, maxPrice, pageable);
    }

    /**
     * Search books by availability with pagination
     */
    @Transactional(readOnly = true)
    public Page<Book> searchBooksByAvailability(Boolean isAvailable, Pageable pageable) {
        log.info("Searching books by availability: {} with pagination: {}", isAvailable, pageable);
        return bookRepository.findByIsAvailable(isAvailable, pageable);
    }

    /**
     * Create a new book
     */
    public Book createBook(BookDto bookDto) {
        log.info("Creating new book: {}", bookDto.getTitle());

        // Check for duplicate book
        if (bookRepository.existsByTitleAndAuthorIgnoreCase(bookDto.getTitle(), bookDto.getAuthor())) {
            throw new DuplicateBookException(bookDto.getTitle(), bookDto.getAuthor());
        }

        Book book = new Book();
        book.setTitle(bookDto.getTitle());
        book.setAuthor(bookDto.getAuthor());
        book.setPublicationYear(bookDto.getPublicationYear());
        book.setDescription(bookDto.getDescription());
        book.setGenre(bookDto.getGenre());
        book.setPageCount(bookDto.getPageCount());
        book.setPrice(bookDto.getPrice() != null ? BigDecimal.valueOf(bookDto.getPrice()) : null);
        book.setIsAvailable(true);

        Book savedBook = bookRepository.save(book);
        log.info("Book created successfully with id: {}", savedBook.getId());
        return savedBook;
    }

    /**
     * Update an existing book
     */
    public Book updateBook(Long id, BookDto bookDto) {
        log.info("Updating book with id: {}", id);

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        // Check for duplicate book (excluding current book)
        if (bookRepository.existsByTitleAndAuthorIgnoreCaseExcludingId(bookDto.getTitle(), bookDto.getAuthor(), id)) {
            throw new DuplicateBookException(bookDto.getTitle(), bookDto.getAuthor());
        }

        book.setTitle(bookDto.getTitle());
        book.setAuthor(bookDto.getAuthor());
        book.setPublicationYear(bookDto.getPublicationYear());
        book.setDescription(bookDto.getDescription());
        book.setGenre(bookDto.getGenre());
        book.setPageCount(bookDto.getPageCount());
        book.setPrice(bookDto.getPrice() != null ? BigDecimal.valueOf(bookDto.getPrice()) : null);

        Book updatedBook = bookRepository.save(book);
        log.info("Book updated successfully");
        return updatedBook;
    }

    /**
     * Update an existing book (legacy method returning Optional)
     */
    public Optional<Book> updateBookOptional(Long id, BookDto bookDto) {
        try {
            Book updatedBook = updateBook(id, bookDto);
            return Optional.of(updatedBook);
        } catch (BookNotFoundException e) {
            return Optional.empty();
        }
    }

    /**
     * Delete a book by ID
     */
    public void deleteBook(Long id) {
        log.info("Deleting book with id: {}", id);
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException(id);
        }
        bookRepository.deleteById(id);
        log.info("Book deleted successfully");
    }

    /**
     * Delete a book by ID (legacy method returning boolean)
     */
    public boolean deleteBookBoolean(Long id) {
        try {
            deleteBook(id);
            return true;
        } catch (BookNotFoundException e) {
            log.warn("Book with id {} not found for deletion", id);
            return false;
        }
    }

    /**
     * Toggle book availability
     */
    public Book toggleBookAvailability(Long id) {
        log.info("Toggling availability for book with id: {}", id);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        book.setIsAvailable(!book.getIsAvailable());
        Book updatedBook = bookRepository.save(book);
        log.info("Book availability toggled to: {}", updatedBook.getIsAvailable());
        return updatedBook;
    }

    /**
     * Check if book exists by ID
     */
    @Transactional(readOnly = true)
    public boolean bookExists(Long id) {
        return bookRepository.existsById(id);
    }

    /**
     * Get book statistics
     */
    @Transactional(readOnly = true)
    public BookStatistics getBookStatistics() {
        log.info("Fetching book statistics");
        return BookStatistics.builder()
                .totalBooks(bookRepository.countAllBooks())
                .availableBooks(bookRepository.countAvailableBooks())
                .averagePrice(bookRepository.getAveragePrice())
                .oldestPublicationYear(bookRepository.getOldestPublicationYear())
                .newestPublicationYear(bookRepository.getNewestPublicationYear())
                .build();
    }

    /**
     * Inner class for book statistics
     */
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class BookStatistics {

        private long totalBooks;
        private long availableBooks;
        private BigDecimal averagePrice;
        private Integer oldestPublicationYear;
        private Integer newestPublicationYear;
    }
}
