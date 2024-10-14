package com.maids.cc.Library.Management.System.BorrowingRecord;

import com.maids.cc.Library.Management.System.BorrowingRecord.exception.BookIsAlreadyBorrowedException;
import com.maids.cc.Library.Management.System.BorrowingRecord.exception.BorrowedRecordDoesNotExist;
import com.maids.cc.Library.Management.System.BorrowingRecord.model.dto.BorrowingRecordDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class BorrowingRecordControllerTest {

    @InjectMocks
    private BorrowingRecordController borrowingRecordController;

    @Mock
    private BorrowingRecordService borrowingRecordService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void borrowBook_ShouldReturnCreated() {
        Long bookId = 1L;
        Long patronId = 1L;

        ResponseEntity<Void> response = borrowingRecordController.borrowBook(bookId, patronId);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(borrowingRecordService, times(1)).borrowBook(bookId, patronId);
    }

    @Test
    void borrowBook_ShouldThrowException_WhenBookIsAlreadyBorrowed() {
        Long bookId = 1L;
        Long patronId = 1L;

        doThrow(new BookIsAlreadyBorrowedException(bookId)).when(borrowingRecordService).borrowBook(bookId, patronId);

        Exception exception = assertThrows(BookIsAlreadyBorrowedException.class, () -> {
            borrowingRecordController.borrowBook(bookId, patronId);
        });

        assertEquals("Book with id " + bookId + " is already borrowed.", exception.getMessage());
    }

    @Test
    void returnBook_ShouldReturnOk() {
        Long bookId = 1L;
        Long patronId = 1L;

        ResponseEntity<Void> response = borrowingRecordController.returnBook(bookId, patronId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(borrowingRecordService, times(1)).returnBook(bookId, patronId);
    }

    @Test
    void returnBook_ShouldThrowException_WhenRecordDoesNotExist() {
        Long bookId = 1L;
        Long patronId = 1L;

        doThrow(new BorrowedRecordDoesNotExist(bookId, patronId)).when(borrowingRecordService).returnBook(bookId, patronId);

        Exception exception = assertThrows(BorrowedRecordDoesNotExist.class, () -> {
            borrowingRecordController.returnBook(bookId, patronId);
        });

        assertEquals("Borrowed record does not exist for bookId " + bookId + " and patronId " + patronId, exception.getMessage());
    }

    @Test
    void getUnreturnedBooks_ShouldReturnList() {
        List<BorrowingRecordDto> mockRecords = Collections.singletonList(new BorrowingRecordDto());
        when(borrowingRecordService.getUnreturnedBooks()).thenReturn(mockRecords);

        ResponseEntity<List<BorrowingRecordDto>> response = borrowingRecordController.getUnreturnedBooks();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockRecords, response.getBody());
        verify(borrowingRecordService, times(1)).getUnreturnedBooks();
    }
}
