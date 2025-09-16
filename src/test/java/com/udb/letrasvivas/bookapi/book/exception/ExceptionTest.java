package com.udb.letrasvivas.bookapi.book.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ExceptionTest {

    @Test
    void BookNotFoundException_WithMessage_ShouldCreateExceptionWithMessage() {
        // When
        BookNotFoundException exception = new BookNotFoundException("Custom error message");

        // Then
        assertThat(exception.getMessage()).isEqualTo("Custom error message");
    }

    @Test
    void BookNotFoundException_WithId_ShouldCreateExceptionWithIdMessage() {
        // When
        BookNotFoundException exception = new BookNotFoundException(123L);

        // Then
        assertThat(exception.getMessage()).isEqualTo("Book with id 123 not found");
    }

    @Test
    void BookNotFoundException_WithMessageAndCause_ShouldCreateExceptionWithMessageAndCause() {
        // Given
        RuntimeException cause = new RuntimeException("Root cause");

        // When
        BookNotFoundException exception = new BookNotFoundException("Custom error message", cause);

        // Then
        assertThat(exception.getMessage()).isEqualTo("Custom error message");
        assertThat(exception.getCause()).isEqualTo(cause);
    }

    @Test
    void DuplicateBookException_WithMessage_ShouldCreateExceptionWithMessage() {
        // When
        DuplicateBookException exception = new DuplicateBookException("Custom duplicate message");

        // Then
        assertThat(exception.getMessage()).isEqualTo("Custom duplicate message");
    }

    @Test
    void DuplicateBookException_WithTitleAndAuthor_ShouldCreateExceptionWithFormattedMessage() {
        // When
        DuplicateBookException exception = new DuplicateBookException("Test Book", "Test Author");

        // Then
        assertThat(exception.getMessage()).isEqualTo("Book with title 'Test Book' by author 'Test Author' already exists");
    }

    @Test
    void DuplicateBookException_WithMessageAndCause_ShouldCreateExceptionWithMessageAndCause() {
        // Given
        RuntimeException cause = new RuntimeException("Root cause");

        // When
        DuplicateBookException exception = new DuplicateBookException("Custom duplicate message", cause);

        // Then
        assertThat(exception.getMessage()).isEqualTo("Custom duplicate message");
        assertThat(exception.getCause()).isEqualTo(cause);
    }
}
