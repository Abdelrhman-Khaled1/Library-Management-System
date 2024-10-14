package com.maids.cc.Library.Management.System.BorrowingRecord;

import static org.junit.jupiter.api.Assertions.*;

import com.maids.cc.Library.Management.System.Books.BookService;
import com.maids.cc.Library.Management.System.Books.model.entity.Book;
import com.maids.cc.Library.Management.System.BorrowingRecord.exception.BookIsAlreadyBorrowedException;
import com.maids.cc.Library.Management.System.BorrowingRecord.exception.BorrowedRecordDoesNotExist;
import com.maids.cc.Library.Management.System.BorrowingRecord.model.dto.BorrowingRecordDto;
import com.maids.cc.Library.Management.System.BorrowingRecord.model.entity.BorrowingRecord;
import com.maids.cc.Library.Management.System.BorrowingRecord.model.mapper.BorrowingRecordMapper;
import com.maids.cc.Library.Management.System.Patrons.PatronService;
import com.maids.cc.Library.Management.System.Patrons.model.entity.Patron;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BorrowingRecordServiceTest {

    @InjectMocks
    private BorrowingRecordService borrowingService;

    @Mock
    private BorrowingRecordRepository borrowingRepository;

    @Mock
    private BookService bookService;

    @Mock
    private PatronService patronService;

    @Mock
    private BorrowingRecordMapper borrowingMapper;


    @Test
    void borrowBook_ShouldThrowException_WhenBookIsAlreadyBorrowed() {
        // Given
        Long bookId = 1L;
        Long patronId = 1L;

        when(borrowingRepository.existsByBookIdAndReturnDateIsNull(bookId)).thenReturn(true);

        // When
        Exception exception = assertThrows(BookIsAlreadyBorrowedException.class, () -> {
            borrowingService.borrowBook(bookId, patronId);
        });

        // Then
        String expectedMessage = "Book with id " + bookId + " is already borrowed.";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage,expectedMessage);

        verify(borrowingRepository, times(1)).existsByBookIdAndReturnDateIsNull(bookId);
        verifyNoMoreInteractions(borrowingRepository);
    }

    @Test
    void borrowBook_ShouldSaveBorrowingRecord_WhenBookIsNotBorrowed() {
        // Given
        Long bookId = 1L;
        Long patronId = 1L;
        Book book = new Book();
        Patron patron = new Patron();

        when(borrowingRepository.existsByBookIdAndReturnDateIsNull(bookId)).thenReturn(false);
        when(bookService.getBookEntityById(bookId)).thenReturn(book);
        when(patronService.getPatronEntityById(patronId)).thenReturn(patron);

        // When
        borrowingService.borrowBook(bookId, patronId);

        // Then
        verify(borrowingRepository, times(1)).save(any(BorrowingRecord.class));
        verify(borrowingRepository, times(1)).existsByBookIdAndReturnDateIsNull(bookId);
        verify(bookService, times(1)).getBookEntityById(bookId);
        verify(patronService, times(1)).getPatronEntityById(patronId);
    }

    @Test
    void returnBook_ShouldThrowException_WhenBorrowingRecordDoesNotExist() {
        // Given
        Long bookId = 1L;
        Long patronId = 1L;

        when(borrowingRepository.existsByBookIdAndPatronIdAndReturnDateIsNull(bookId, patronId)).thenReturn(false);

        // When
        Exception exception = assertThrows(BorrowedRecordDoesNotExist.class, () -> {
            borrowingService.returnBook(bookId, patronId);
        });

        // Then
        String expectedMessage = "Borrowed record does not exist for bookId " + bookId + " and patronId " + patronId;
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage,expectedMessage);

        verify(borrowingRepository, times(1)).existsByBookIdAndPatronIdAndReturnDateIsNull(bookId, patronId);
        verifyNoMoreInteractions(borrowingRepository);
    }

    @Test
    void returnBook_ShouldUpdateBorrowingRecord_WhenExists() {
        // Given
        Long bookId = 1L;
        Long patronId = 1L;
        BorrowingRecord borrowingRecord = new BorrowingRecord();

        when(borrowingRepository.existsByBookIdAndPatronIdAndReturnDateIsNull(bookId, patronId)).thenReturn(true);
        when(borrowingRepository.findByBookIdAndPatronIdAndReturnDateIsNull(bookId, patronId)).thenReturn(borrowingRecord);

        // When
        borrowingService.returnBook(bookId, patronId);

        // Then
        assertTrue(borrowingRecord.isReturned());
        verify(borrowingRepository, times(1)).save(borrowingRecord);
        verify(borrowingRepository, times(1)).findByBookIdAndPatronIdAndReturnDateIsNull(bookId, patronId);
    }


    @Test
    void testGetUnreturnedBooks_NoRecords() {
        // Arrange
        when(borrowingRepository.findByReturned(false)).thenReturn(Collections.emptyList());

        // Act
        List<BorrowingRecordDto> result = borrowingService.getUnreturnedBooks();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected empty list of unreturned books.");
        verify(borrowingRepository).findByReturned(false);
    }

    @Test
    void testGetUnreturnedBooks_WithRecords() {
        // Arrange
        BorrowingRecord record = new BorrowingRecord();
        record.setId(1L);
        record.setReturned(false);
        record.setBorrowDate(LocalDate.now());

        BorrowingRecordDto recordDto = new BorrowingRecordDto();
        recordDto.setId(1L);
        recordDto.setReturned(false);

        when(borrowingRepository.findByReturned(false)).thenReturn(List.of(record));
        when(borrowingMapper.toDto(record)).thenReturn(recordDto);

        // Act
        List<BorrowingRecordDto> result = borrowingService.getUnreturnedBooks();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size(), "Expected one unreturned book.");
        assertEquals(recordDto, result.get(0), "Expected the mapped DTO to match.");
        verify(borrowingRepository).findByReturned(false);
        verify(borrowingMapper).toDto(record);
    }

}
