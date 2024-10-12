package com.maids.cc.Library.Management.System.Books.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddBookDto {
    private String title;
    private String author;
    private Integer publicationYear;
    private String isbn;
}
