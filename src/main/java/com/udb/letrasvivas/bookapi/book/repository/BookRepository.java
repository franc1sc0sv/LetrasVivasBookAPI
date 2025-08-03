package com.udb.letrasvivas.bookapi.book.repository;

import java.util.List;

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
     * Find books by author containing the given text (case-insensitive)
     */
    @Query("SELECT b FROM Book b WHERE LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%'))")
    List<Book> findByAuthorContainingIgnoreCase(@Param("author") String author);

    /**
     * Find books by publication year
     */
    List<Book> findByPublicationYear(int publicationYear);

    /**
     * Find books by publication year range
     */
    List<Book> findByPublicationYearBetween(int startYear, int endYear);
}
