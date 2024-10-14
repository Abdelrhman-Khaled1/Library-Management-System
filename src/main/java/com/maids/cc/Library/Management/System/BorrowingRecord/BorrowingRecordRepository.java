package com.maids.cc.Library.Management.System.BorrowingRecord;

import com.maids.cc.Library.Management.System.BorrowingRecord.model.entity.BorrowingRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord, Long> {

    List<BorrowingRecord> findByReturned(boolean returned);

    boolean existsByBookIdAndReturnDateIsNull(Long bookId);

    boolean existsByBookIdAndPatronIdAndReturnDateIsNull(Long bookId, Long patronId);

    BorrowingRecord findByBookIdAndPatronIdAndReturnDateIsNull(Long bookId, Long patronId);
}
