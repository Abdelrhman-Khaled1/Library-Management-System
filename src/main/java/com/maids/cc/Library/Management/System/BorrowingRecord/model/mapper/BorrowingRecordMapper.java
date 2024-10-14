package com.maids.cc.Library.Management.System.BorrowingRecord.model.mapper;

import com.maids.cc.Library.Management.System.BorrowingRecord.model.dto.BorrowingRecordDto;
import com.maids.cc.Library.Management.System.BorrowingRecord.model.entity.BorrowingRecord;
import org.springframework.stereotype.Component;

@Component
public class BorrowingRecordMapper {

    public BorrowingRecordDto toDto(BorrowingRecord borrowingRecord) {
        BorrowingRecordDto dto = new BorrowingRecordDto();

        dto.setId(borrowingRecord.getId());
        dto.setBookId(borrowingRecord.getBook().getId());
        dto.setBookTitle(borrowingRecord.getBook().getTitle());
        dto.setPatronId(borrowingRecord.getPatron().getId());
        dto.setPatronName(borrowingRecord.getPatron().getName());
        dto.setReturned(borrowingRecord.isReturned());

        return dto;
    }

}
