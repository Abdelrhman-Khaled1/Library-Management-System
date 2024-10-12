package com.maids.cc.Library.Management.System.Books;

import com.maids.cc.Library.Management.System.Books.model.dto.AddBookDto;
import com.maids.cc.Library.Management.System.Books.model.dto.BookDto;
import com.maids.cc.Library.Management.System.Books.model.dto.UpdateBookDto;
import com.maids.cc.Library.Management.System.Books.model.entity.Book;
import com.maids.cc.Library.Management.System.Books.model.mapper.BookMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;


    public List<BookDto> getAllBooks() {
        return bookMapper.toDtos(bookRepository.findAll());
    }

    public BookDto getBookById(Long id) {
        Optional<Book> book = bookRepository.findById(id);
        return bookMapper.toDto(book.get());
    }

    public BookDto saveBook(AddBookDto bookDto) {
        Book addBook = bookMapper.toEntity(bookDto);
        Book savedBook = bookRepository.save(addBook);
        return bookMapper.toDto(savedBook);
    }

    public BookDto updateBook(Long id, UpdateBookDto updatedBookDto) {
        Book updatedBook = bookMapper.toEntity(updatedBookDto);
        updatedBook.setId(id);
        Book book = bookRepository.save(updatedBook);
        return bookMapper.toDto(book);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}
