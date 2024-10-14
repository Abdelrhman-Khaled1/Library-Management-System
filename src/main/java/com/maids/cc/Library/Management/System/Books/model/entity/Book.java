package com.maids.cc.Library.Management.System.Books.model.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.maids.cc.Library.Management.System.BorrowingRecord.model.entity.BorrowingRecord;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "books")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private Integer publicationYear;
    private String isbn;

    @JsonIgnore
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<BorrowingRecord> borrowingRecords;
}
