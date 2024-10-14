package com.maids.cc.Library.Management.System.BorrowingRecord.exception;

public class BorrowedRecordDoesNotExist extends RuntimeException{

    public BorrowedRecordDoesNotExist(long bookId, long patronId) {
        super("Borrowed record does not exist for bookId " + bookId + " and patronId " + patronId);
    }
}
