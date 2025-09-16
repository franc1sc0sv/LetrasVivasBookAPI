package com.udb.letrasvivas.bookapi.book.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(String message) {
        super(message);
    }

    public BookNotFoundException(Long id) {
        super("Book with id " + id + " not found");
    }

    public BookNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
