package com.maids.cc.Library.Management.System.Books;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Book not found")
public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(Long bookId) {
        super("Book with Book Id " + bookId + " Not Found!");
    }
}
