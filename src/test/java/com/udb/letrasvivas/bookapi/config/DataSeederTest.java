package com.udb.letrasvivas.bookapi.config;

import com.udb.letrasvivas.bookapi.book.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class DataSeederTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private DataSeeder dataSeeder;

    @Test
    void testDataSeederSkipsWhenDatabaseHasData() throws Exception {
        // Given: Database with existing data (seeder runs automatically on startup)
        long initialCount = bookRepository.count();
        assertThat(initialCount).isEqualTo(100);

        // When: Run the seeder again
        dataSeeder.run();

        // Then: Database should still contain only 100 books (no duplicates)
        assertThat(bookRepository.count()).isEqualTo(100);
    }

    @Test
    void testSeededBooksHaveValidData() throws Exception {
        // Given: Database with seeded data (seeder runs automatically on startup)
        var books = bookRepository.findAll();
        assertThat(books).hasSize(100);

        // Then: All books should have valid data
        books.forEach(book -> {
            assertThat(book.getTitle()).isNotBlank();
            assertThat(book.getAuthor()).isNotBlank();
            assertThat(book.getPublicationYear()).isBetween(1800, 2024);
            assertThat(book.getPageCount()).isBetween(100, 1000);
            assertThat(book.getPrice()).isBetween(java.math.BigDecimal.valueOf(5.99), java.math.BigDecimal.valueOf(51.00));
            assertThat(book.getIsAvailable()).isNotNull();
            assertThat(book.getCreatedAt()).isNotNull();
            assertThat(book.getUpdatedAt()).isNotNull();
            assertThat(book.getVersion()).isEqualTo(0L);
        });
    }

    @Test
    void testSeededBooksHaveDiverseData() throws Exception {
        // Given: Database with seeded data
        var books = bookRepository.findAll();
        assertThat(books).hasSize(100);

        // Then: Books should have diverse data
        var titles = books.stream().map(book -> book.getTitle()).distinct().count();
        var authors = books.stream().map(book -> book.getAuthor()).distinct().count();
        var genres = books.stream().map(book -> book.getGenre()).distinct().count();

        // Should have some diversity in the data
        assertThat(titles).isGreaterThan(1);
        assertThat(authors).isGreaterThan(1);
        assertThat(genres).isGreaterThan(1);
    }
}
