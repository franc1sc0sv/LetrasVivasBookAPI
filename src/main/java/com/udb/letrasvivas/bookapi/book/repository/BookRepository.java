package com.udb.letrasvivas.bookapi.book.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.udb.letrasvivas.bookapi.book.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * Find books by title containing the given text (case-insensitive)
     */
    @Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Book> findByTitleContainingIgnoreCase(@Param("title") String title);

    /**
     * Find books by title containing the given text with pagination
     * (case-insensitive)
     */
    @Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    Page<Book> findByTitleContainingIgnoreCase(@Param("title") String title, Pageable pageable);

    /**
     * Find books by author containing the given text (case-insensitive)
     */
    @Query("SELECT b FROM Book b WHERE LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%'))")
    List<Book> findByAuthorContainingIgnoreCase(@Param("author") String author);

    /**
     * Find books by author containing the given text with pagination
     * (case-insensitive)
     */
    @Query("SELECT b FROM Book b WHERE LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%'))")
    Page<Book> findByAuthorContainingIgnoreCase(@Param("author") String author, Pageable pageable);

    /**
     * Find books by publication year
     */
    List<Book> findByPublicationYear(Integer publicationYear);

    /**
     * Find books by publication year with pagination
     */
    Page<Book> findByPublicationYear(Integer publicationYear, Pageable pageable);

    /**
     * Find books by publication year range
     */
    List<Book> findByPublicationYearBetween(Integer startYear, Integer endYear);

    /**
     * Find books by publication year range with pagination
     */
    Page<Book> findByPublicationYearBetween(Integer startYear, Integer endYear, Pageable pageable);

    /**
     * Find books by genre (case-insensitive)
     */
    @Query("SELECT b FROM Book b WHERE LOWER(b.genre) = LOWER(:genre)")
    List<Book> findByGenreIgnoreCase(@Param("genre") String genre);

    /**
     * Find books by genre with pagination (case-insensitive)
     */
    @Query("SELECT b FROM Book b WHERE LOWER(b.genre) = LOWER(:genre)")
    Page<Book> findByGenreIgnoreCase(@Param("genre") String genre, Pageable pageable);

    /**
     * Find books by availability status
     */
    List<Book> findByIsAvailable(Boolean isAvailable);

    /**
     * Find books by availability status with pagination
     */
    Page<Book> findByIsAvailable(Boolean isAvailable, Pageable pageable);

    /**
     * Find books by price range
     */
    List<Book> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    /**
     * Find books by price range with pagination
     */
    Page<Book> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    /**
     * Find books by page count range
     */
    List<Book> findByPageCountBetween(Integer minPages, Integer maxPages);

    /**
     * Find books by page count range with pagination
     */
    Page<Book> findByPageCountBetween(Integer minPages, Integer maxPages, Pageable pageable);

    /**
     * Advanced search with multiple criteria
     */
    @Query("SELECT b FROM Book b WHERE "
            + "(:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND "
            + "(:author IS NULL OR LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%'))) AND "
            + "(:genre IS NULL OR LOWER(b.genre) = LOWER(:genre)) AND "
            + "(:minYear IS NULL OR b.publicationYear >= :minYear) AND "
            + "(:maxYear IS NULL OR b.publicationYear <= :maxYear) AND "
            + "(:minPrice IS NULL OR b.price >= :minPrice) AND "
            + "(:maxPrice IS NULL OR b.price <= :maxPrice) AND "
            + "(:isAvailable IS NULL OR b.isAvailable = :isAvailable)")
    Page<Book> findBooksWithAdvancedSearch(
            @Param("title") String title,
            @Param("author") String author,
            @Param("genre") String genre,
            @Param("minYear") Integer minYear,
            @Param("maxYear") Integer maxYear,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("isAvailable") Boolean isAvailable,
            Pageable pageable);

    /**
     * Check if a book with the same title and author exists
     */
    @Query("SELECT COUNT(b) > 0 FROM Book b WHERE LOWER(b.title) = LOWER(:title) AND LOWER(b.author) = LOWER(:author)")
    boolean existsByTitleAndAuthorIgnoreCase(@Param("title") String title, @Param("author") String author);

    /**
     * Check if a book with the same title and author exists (excluding a
     * specific ID)
     */
    @Query("SELECT COUNT(b) > 0 FROM Book b WHERE LOWER(b.title) = LOWER(:title) AND LOWER(b.author) = LOWER(:author) AND b.id != :id")
    boolean existsByTitleAndAuthorIgnoreCaseExcludingId(@Param("title") String title, @Param("author") String author, @Param("id") Long id);

    /**
     * Find books by title and author (exact match, case-insensitive)
     */
    @Query("SELECT b FROM Book b WHERE LOWER(b.title) = LOWER(:title) AND LOWER(b.author) = LOWER(:author)")
    Optional<Book> findByTitleAndAuthorIgnoreCase(@Param("title") String title, @Param("author") String author);

    /**
     * Get statistics about books
     */
    @Query("SELECT COUNT(b) FROM Book b")
    long countAllBooks();

    @Query("SELECT COUNT(b) FROM Book b WHERE b.isAvailable = true")
    long countAvailableBooks();

    @Query("SELECT AVG(b.price) FROM Book b WHERE b.price IS NOT NULL")
    BigDecimal getAveragePrice();

    @Query("SELECT MIN(b.publicationYear) FROM Book b")
    Integer getOldestPublicationYear();

    @Query("SELECT MAX(b.publicationYear) FROM Book b")
    Integer getNewestPublicationYear();
}
