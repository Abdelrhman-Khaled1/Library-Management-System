package com.maids.cc.Library.Management.System.BorrowingRecord;

import com.maids.cc.Library.Management.System.Books.BookService;
import com.maids.cc.Library.Management.System.Books.model.entity.Book;
import com.maids.cc.Library.Management.System.BorrowingRecord.exception.BookIsAlreadyBorrowedException;
import com.maids.cc.Library.Management.System.BorrowingRecord.exception.BorrowedRecordDoesNotExist;
import com.maids.cc.Library.Management.System.BorrowingRecord.model.dto.BorrowingRecordDto;
import com.maids.cc.Library.Management.System.BorrowingRecord.model.entity.BorrowingRecord;
import com.maids.cc.Library.Management.System.BorrowingRecord.model.mapper.BorrowingRecordMapper;
import com.maids.cc.Library.Management.System.Patrons.PatronService;
import com.maids.cc.Library.Management.System.Patrons.model.entity.Patron;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BorrowingRecordService {

    private final BorrowingRecordRepository borrowingRepository;
    private final BookService bookService;
    private final PatronService patronService;
    private final BorrowingRecordMapper borrowingMapper;

    @Transactional
    public void borrowBook(Long bookId, Long patronId) {
        if (borrowingRepository.existsByBookIdAndReturnDateIsNull(bookId))
            throw new BookIsAlreadyBorrowedException(bookId);

        Book book = bookService.getBookEntityById(bookId);
        Patron patron = patronService.getPatronEntityById(patronId);

        BorrowingRecord borrowingRecord = new BorrowingRecord();
        borrowingRecord.setBook(book);
        borrowingRecord.setPatron(patron);
        borrowingRecord.setBorrowDate(LocalDate.now());

        borrowingRepository.save(borrowingRecord);
    }

    @Transactional
    public void returnBook(Long bookId, Long patronId) {
        if (!borrowingRepository.existsByBookIdAndPatronIdAndReturnDateIsNull(bookId, patronId))
            throw new BorrowedRecordDoesNotExist(bookId, patronId);

        BorrowingRecord borrowingRecord = borrowingRepository.findByBookIdAndPatronIdAndReturnDateIsNull(bookId, patronId);

        borrowingRecord.setReturned(true);
        borrowingRecord.setReturnDate(LocalDate.now());

        borrowingRepository.save(borrowingRecord);
    }

    public List<BorrowingRecordDto> getUnreturnedBooks() {
        List<BorrowingRecord> unreturnedRecords = borrowingRepository.findByReturned(false);
        return unreturnedRecords.stream()
                .map(borrowingMapper::toDto)
                .toList();
    }
}
