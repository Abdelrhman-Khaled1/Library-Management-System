package com.maids.cc.Library.Management.System.Books;

import com.maids.cc.Library.Management.System.Books.model.dto.AddBookDto;
import com.maids.cc.Library.Management.System.Books.model.dto.BookDto;
import com.maids.cc.Library.Management.System.Books.model.dto.UpdateBookDto;
import com.maids.cc.Library.Management.System.Books.model.entity.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public List<BookDto> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable Long id) {
        BookDto book = bookService.getBookById(id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(book);
    }

    @PostMapping
    public ResponseEntity<BookDto> addBook(@RequestBody AddBookDto bookDto) {
        BookDto savedBook = bookService.saveBook(bookDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDto> updateBook(@PathVariable Long id, @RequestBody UpdateBookDto updatedBookDto) {
        BookDto updated = bookService.updateBook(id, updatedBookDto);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
