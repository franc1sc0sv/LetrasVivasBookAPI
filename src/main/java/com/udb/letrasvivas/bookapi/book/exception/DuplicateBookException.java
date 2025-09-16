package com.udb.letrasvivas.bookapi.book.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateBookException extends RuntimeException {

    public DuplicateBookException(String message) {
        super(message);
    }

    public DuplicateBookException(String title, String author) {
        super("Book with title '" + title + "' by author '" + author + "' already exists");
    }

    public DuplicateBookException(String message, Throwable cause) {
        super(message, cause);
    }
}
