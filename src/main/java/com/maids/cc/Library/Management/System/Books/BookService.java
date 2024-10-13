package com.maids.cc.Library.Management.System.Books;

import com.maids.cc.Library.Management.System.Books.model.dto.AddBookDto;
import com.maids.cc.Library.Management.System.Books.model.dto.BookDto;
import com.maids.cc.Library.Management.System.Books.model.dto.UpdateBookDto;
import com.maids.cc.Library.Management.System.Books.model.entity.Book;
import com.maids.cc.Library.Management.System.Books.model.mapper.BookMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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

    @Cacheable(value = "books", key = "#id", unless = "#result == null")
    public BookDto getBookById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID must be greater than 0");
        }

        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            return bookMapper.toDto(book.get());
        } else {
            throw new BookNotFoundException(id);
        }
    }

    public BookDto saveBook(AddBookDto bookDto) {
        Book addBook = bookMapper.toEntity(bookDto);
        Book savedBook = bookRepository.save(addBook);
        return bookMapper.toDto(savedBook);
    }

    @CachePut(value = "books", key = "#id")
    public BookDto updateBook(Long id, UpdateBookDto updatedBookDto) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID must be greater than 0"); // or create a custom exception
        }
        if (bookRepository.findById(id).isPresent()) {
            Book updatedBook = bookMapper.toEntity(updatedBookDto);
            updatedBook.setId(id);
            Book book = bookRepository.save(updatedBook);
            return bookMapper.toDto(book);
        }
        throw new BookNotFoundException(id);
    }

    @CacheEvict(value = "books", key = "#id")
    public void deleteBook(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID must be greater than 0"); // or create a custom exception
        }

        if (bookRepository.findById(id).isPresent()) {
            bookRepository.deleteById(id);
        } else {
            throw new BookNotFoundException(id);
        }
    }
}
