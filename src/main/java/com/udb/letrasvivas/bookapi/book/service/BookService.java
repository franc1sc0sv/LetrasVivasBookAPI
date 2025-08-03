package com.udb.letrasvivas.bookapi.book.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.udb.letrasvivas.bookapi.book.dto.BookDto;
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
     * Get all books
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
    public Optional<Book> getBookById(Long id) {
        log.info("Fetching book with id: {}", id);
        return bookRepository.findById(id);
    }

    /**
     * Search books by title
     */
    @Transactional(readOnly = true)
    public List<Book> searchBooksByTitle(String title) {
        log.info("Searching books with title containing: {}", title);
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    /**
     * Search books by author
     */
    @Transactional(readOnly = true)
    public List<Book> searchBooksByAuthor(String author) {
        log.info("Searching books with author containing: {}", author);
        return bookRepository.findByAuthorContainingIgnoreCase(author);
    }

    /**
     * Create a new book
     */
    public Book createBook(BookDto bookDto) {
        log.info("Creating new book: {}", bookDto.getTitle());
        Book book = new Book();
        book.setTitle(bookDto.getTitle());
        book.setAuthor(bookDto.getAuthor());
        book.setPublicationYear(bookDto.getPublicationYear());

        Book savedBook = bookRepository.save(book);
        log.info("Book created successfully with id: {}", savedBook.getId());
        return savedBook;
    }

    /**
     * Update an existing book
     */
    public Optional<Book> updateBook(Long id, BookDto bookDto) {
        log.info("Updating book with id: {}", id);
        return bookRepository.findById(id)
                .map(book -> {
                    book.setTitle(bookDto.getTitle());
                    book.setAuthor(bookDto.getAuthor());
                    book.setPublicationYear(bookDto.getPublicationYear());
                    Book updatedBook = bookRepository.save(book);
                    log.info("Book updated successfully");
                    return updatedBook;
                });
    }

    /**
     * Delete a book by ID
     */
    public boolean deleteBook(Long id) {
        log.info("Deleting book with id: {}", id);
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            log.info("Book deleted successfully");
            return true;
        }
        log.warn("Book with id {} not found for deletion", id);
        return false;
    }

    /**
     * Check if book exists by ID
     */
    @Transactional(readOnly = true)
    public boolean bookExists(Long id) {
        return bookRepository.existsById(id);
    }
}
