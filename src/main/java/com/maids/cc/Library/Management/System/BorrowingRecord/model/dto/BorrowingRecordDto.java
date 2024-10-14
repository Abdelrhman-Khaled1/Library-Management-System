package com.maids.cc.Library.Management.System.BorrowingRecord.model.dto;

import lombok.Data;

@Data
public class BorrowingRecordDto {
    private Long id;
    private Long bookId;
    private String bookTitle;
    private Long patronId;
    private String patronName;
    private boolean returned;
}
