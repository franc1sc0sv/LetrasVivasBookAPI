package com.udb.letrasvivas.bookapi.book.repository;

import com.udb.letrasvivas.bookapi.book.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class BookRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookRepository bookRepository;

    private Book testBook1;
    private Book testBook2;
    private Book testBook3;

    @BeforeEach
    void setUp() {
        // Create test books
        testBook1 = createTestBook("The Great Gatsby", "F. Scott Fitzgerald", 1925, "Fiction", BigDecimal.valueOf(12.99), true);
        testBook2 = createTestBook("1984", "George Orwell", 1949, "Science Fiction", BigDecimal.valueOf(14.99), true);
        testBook3 = createTestBook("To Kill a Mockingbird", "Harper Lee", 1960, "Fiction", BigDecimal.valueOf(13.99), false);

        // Save test books
        entityManager.persistAndFlush(testBook1);
        entityManager.persistAndFlush(testBook2);
        entityManager.persistAndFlush(testBook3);
    }

    @Test
    void findByTitleContainingIgnoreCase_ShouldReturnMatchingBooks() {
        // When
        List<Book> result = bookRepository.findByTitleContainingIgnoreCase("Gatsby");

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("The Great Gatsby");
    }

    @Test
    void findByTitleContainingIgnoreCase_WithPagination_ShouldReturnPaginatedResults() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Book> result = bookRepository.findByTitleContainingIgnoreCase("The", pageable);

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    void findByAuthorContainingIgnoreCase_ShouldReturnMatchingBooks() {
        // When
        List<Book> result = bookRepository.findByAuthorContainingIgnoreCase("Orwell");

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAuthor()).isEqualTo("George Orwell");
    }

    @Test
    void findByAuthorContainingIgnoreCase_WithPagination_ShouldReturnPaginatedResults() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Book> result = bookRepository.findByAuthorContainingIgnoreCase("Scott", pageable);

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    void findByPublicationYear_ShouldReturnMatchingBooks() {
        // When
        List<Book> result = bookRepository.findByPublicationYear(1925);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("The Great Gatsby");
    }

    @Test
    void findByPublicationYear_WithPagination_ShouldReturnPaginatedResults() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Book> result = bookRepository.findByPublicationYear(1949, pageable);

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    void findByPublicationYearBetween_ShouldReturnMatchingBooks() {
        // When
        List<Book> result = bookRepository.findByPublicationYearBetween(1940, 1970);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).extracting(Book::getTitle)
                .containsExactlyInAnyOrder("1984", "To Kill a Mockingbird");
    }

    @Test
    void findByPublicationYearBetween_WithPagination_ShouldReturnPaginatedResults() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Book> result = bookRepository.findByPublicationYearBetween(1940, 1970, pageable);

        // Then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(2);
    }

    @Test
    void findByGenreIgnoreCase_ShouldReturnMatchingBooks() {
        // When
        List<Book> result = bookRepository.findByGenreIgnoreCase("Fiction");

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).extracting(Book::getTitle)
                .containsExactlyInAnyOrder("The Great Gatsby", "To Kill a Mockingbird");
    }

    @Test
    void findByGenreIgnoreCase_WithPagination_ShouldReturnPaginatedResults() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Book> result = bookRepository.findByGenreIgnoreCase("Science Fiction", pageable);

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    void findByIsAvailable_ShouldReturnMatchingBooks() {
        // When
        List<Book> result = bookRepository.findByIsAvailable(true);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).extracting(Book::getTitle)
                .containsExactlyInAnyOrder("The Great Gatsby", "1984");
    }

    @Test
    void findByIsAvailable_WithPagination_ShouldReturnPaginatedResults() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Book> result = bookRepository.findByIsAvailable(false, pageable);

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    void findByPriceBetween_ShouldReturnMatchingBooks() {
        // When
        List<Book> result = bookRepository.findByPriceBetween(BigDecimal.valueOf(12.0), BigDecimal.valueOf(15.0));

        // Then
        assertThat(result).hasSize(3);
        assertThat(result).extracting(Book::getTitle)
                .containsExactlyInAnyOrder("The Great Gatsby", "1984", "To Kill a Mockingbird");
    }

    @Test
    void findByPriceBetween_WithPagination_ShouldReturnPaginatedResults() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Book> result = bookRepository.findByPriceBetween(BigDecimal.valueOf(12.0), BigDecimal.valueOf(14.0), pageable);

        // Then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(2);
    }

    @Test
    void findByPageCountBetween_ShouldReturnMatchingBooks() {
        // When
        List<Book> result = bookRepository.findByPageCountBetween(200, 400);

        // Then
        assertThat(result).hasSize(3);
    }

    @Test
    void findByPageCountBetween_WithPagination_ShouldReturnPaginatedResults() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Book> result = bookRepository.findByPageCountBetween(200, 300, pageable);

        // Then
        assertThat(result.getContent()).hasSize(3);
        assertThat(result.getTotalElements()).isEqualTo(3);
    }

    @Test
    void findBooksWithAdvancedSearch_WithAllCriteria_ShouldReturnFilteredResults() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Book> result = bookRepository.findBooksWithAdvancedSearch(
                "Gatsby", "Fitzgerald", "Fiction", 1920, 1930,
                BigDecimal.valueOf(10.0), BigDecimal.valueOf(20.0), true, pageable);

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("The Great Gatsby");
    }

    @Test
    void findBooksWithAdvancedSearch_WithPartialCriteria_ShouldReturnFilteredResults() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Book> result = bookRepository.findBooksWithAdvancedSearch(
                null, null, "Fiction", null, null,
                null, null, null, pageable);

        // Then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).extracting(Book::getTitle)
                .containsExactlyInAnyOrder("The Great Gatsby", "To Kill a Mockingbird");
    }

    @Test
    void existsByTitleAndAuthorIgnoreCase_WhenBookExists_ShouldReturnTrue() {
        // When
        boolean result = bookRepository.existsByTitleAndAuthorIgnoreCase("The Great Gatsby", "F. Scott Fitzgerald");

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void existsByTitleAndAuthorIgnoreCase_WhenBookDoesNotExist_ShouldReturnFalse() {
        // When
        boolean result = bookRepository.existsByTitleAndAuthorIgnoreCase("Non-existent Book", "Unknown Author");

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void existsByTitleAndAuthorIgnoreCaseExcludingId_WhenBookExists_ShouldReturnTrue() {
        // When
        boolean result = bookRepository.existsByTitleAndAuthorIgnoreCaseExcludingId(
                "The Great Gatsby", "F. Scott Fitzgerald", 999L);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void existsByTitleAndAuthorIgnoreCaseExcludingId_WhenExcludingSameBook_ShouldReturnFalse() {
        // When
        boolean result = bookRepository.existsByTitleAndAuthorIgnoreCaseExcludingId(
                "The Great Gatsby", "F. Scott Fitzgerald", testBook1.getId());

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void findByTitleAndAuthorIgnoreCase_WhenBookExists_ShouldReturnBook() {
        // When
        Optional<Book> result = bookRepository.findByTitleAndAuthorIgnoreCase("The Great Gatsby", "F. Scott Fitzgerald");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("The Great Gatsby");
    }

    @Test
    void findByTitleAndAuthorIgnoreCase_WhenBookDoesNotExist_ShouldReturnEmpty() {
        // When
        Optional<Book> result = bookRepository.findByTitleAndAuthorIgnoreCase("Non-existent Book", "Unknown Author");

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void countAllBooks_ShouldReturnCorrectCount() {
        // When
        long result = bookRepository.countAllBooks();

        // Then
        assertThat(result).isEqualTo(3);
    }

    @Test
    void countAvailableBooks_ShouldReturnCorrectCount() {
        // When
        long result = bookRepository.countAvailableBooks();

        // Then
        assertThat(result).isEqualTo(2);
    }

    @Test
    void getAveragePrice_ShouldReturnCorrectAverage() {
        // When
        BigDecimal result = bookRepository.getAveragePrice();

        // Then
        assertThat(result).isNotNull();
        // Average of 12.99, 14.99, 13.99 = 13.99
        assertThat(result).isEqualByComparingTo(BigDecimal.valueOf(13.99));
    }

    @Test
    void getOldestPublicationYear_ShouldReturnCorrectYear() {
        // When
        Integer result = bookRepository.getOldestPublicationYear();

        // Then
        assertThat(result).isEqualTo(1925);
    }

    @Test
    void getNewestPublicationYear_ShouldReturnCorrectYear() {
        // When
        Integer result = bookRepository.getNewestPublicationYear();

        // Then
        assertThat(result).isEqualTo(1960);
    }

    @Test
    void findByTitleContainingIgnoreCase_ShouldBeCaseInsensitive() {
        // When
        List<Book> result = bookRepository.findByTitleContainingIgnoreCase("gatsby");

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("The Great Gatsby");
    }

    @Test
    void findByAuthorContainingIgnoreCase_ShouldBeCaseInsensitive() {
        // When
        List<Book> result = bookRepository.findByAuthorContainingIgnoreCase("orwell");

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAuthor()).isEqualTo("George Orwell");
    }

    @Test
    void findByGenreIgnoreCase_ShouldBeCaseInsensitive() {
        // When
        List<Book> result = bookRepository.findByGenreIgnoreCase("fiction");

        // Then
        assertThat(result).hasSize(2);
    }

    private Book createTestBook(String title, String author, Integer publicationYear, String genre, BigDecimal price, Boolean isAvailable) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setPublicationYear(publicationYear);
        book.setDescription("Test description for " + title);
        book.setGenre(genre);
        book.setPageCount(300);
        book.setPrice(price);
        book.setIsAvailable(isAvailable);
        book.setCreatedAt(LocalDateTime.now());
        book.setUpdatedAt(LocalDateTime.now());
        book.setVersion(1L);
        return book;
    }
}
