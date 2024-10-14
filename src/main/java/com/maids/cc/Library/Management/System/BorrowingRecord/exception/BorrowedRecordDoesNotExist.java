package com.maids.cc.Library.Management.System.BorrowingRecord.exception;

public class BorrowedRecordDoesNotExist extends RuntimeException{

    public BorrowedRecordDoesNotExist(long bookId, long patronId) {
        super("patron id "+patronId+" didn't borrow a book with id "+ bookId +".");
    }
}
