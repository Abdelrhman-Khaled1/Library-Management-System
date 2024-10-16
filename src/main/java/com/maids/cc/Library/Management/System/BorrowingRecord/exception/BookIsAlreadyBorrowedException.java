package com.maids.cc.Library.Management.System.BorrowingRecord.exception;

public class BookIsAlreadyBorrowedException extends RuntimeException {

    public BookIsAlreadyBorrowedException(long bookId) {
        super("Book with id "+ bookId +" is already borrowed.");
    }
}
