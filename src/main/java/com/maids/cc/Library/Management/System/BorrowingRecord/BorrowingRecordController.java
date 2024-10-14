package com.maids.cc.Library.Management.System.BorrowingRecord;

import com.maids.cc.Library.Management.System.BorrowingRecord.model.dto.BorrowingRecordDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BorrowingRecordController {

    private final BorrowingRecordService borrowingService;

    @PostMapping("/borrow/{bookId}/patron/{patronId}")
    public ResponseEntity<Void> borrowBook(@PathVariable Long bookId, @PathVariable Long patronId) {
        borrowingService.borrowBook(bookId, patronId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/return/{bookId}/patron/{patronId}")
    public ResponseEntity<Void> returnBook(@PathVariable Long bookId, @PathVariable Long patronId) {
        borrowingService.returnBook(bookId, patronId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/unreturned")
    public ResponseEntity<List<BorrowingRecordDto>> getUnreturnedBooks() {
        List<BorrowingRecordDto> unreturnedBooks = borrowingService.getUnreturnedBooks();
        return ResponseEntity.ok(unreturnedBooks);
    }
}
