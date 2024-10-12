package com.maids.cc.Library.Management.System.Books;

import com.maids.cc.Library.Management.System.Books.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book,Long> {
}
