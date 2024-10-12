package com.maids.cc.Library.Management.System.Books.model.entity;


import jakarta.persistence.*;
import lombok.*;

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
}
