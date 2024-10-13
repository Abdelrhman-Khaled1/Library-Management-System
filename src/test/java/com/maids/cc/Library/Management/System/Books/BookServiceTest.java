package com.maids.cc.Library.Management.System.Books;

import com.maids.cc.Library.Management.System.Books.model.dto.AddBookDto;
import com.maids.cc.Library.Management.System.Books.model.dto.BookDto;
import com.maids.cc.Library.Management.System.Books.model.dto.UpdateBookDto;
import com.maids.cc.Library.Management.System.Books.model.entity.Book;
import com.maids.cc.Library.Management.System.Books.model.mapper.BookMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookService bookService;


    @Test
    void testGetAllBooks() {
        //Arrange
        List<Book> books = List.of(new Book(), new Book());
        when(bookRepository.findAll()).thenReturn(books);
        List<BookDto> bookDtos = List.of(new BookDto(), new BookDto());
        when(bookMapper.toDtos(books)).thenReturn(bookDtos);

        // Action
        List<BookDto> result = bookService.getAllBooks();

        // Assertion
        assertEquals(2, result.size());
        verify(bookRepository).findAll();
        verify(bookMapper).toDtos(books);

        verify(bookRepository, times(1)).findAll();
        verify(bookMapper, times(1)).toDtos(books);
    }

    @Test
    void testGetBookById() {
        //Arrange
        Book book = Book.builder()
                .id(1L)
                .title("Harry Potter and the Sorcerer's Stone")
                .author("J.K. Rowling")
                .isbn("1234567891234")
                .publicationYear(2012)
                .build();
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        BookDto expectedBookDto = BookDto.builder()
                .id(1L)
                .title("Harry Potter and the Sorcerer's Stone")
                .author("J.K. Rowling")
                .isbn("1234567891234")
                .publicationYear(2012)
                .build();
        when(bookMapper.toDto(any(Book.class))).thenReturn(expectedBookDto);


        //Act
        BookDto actualBookDto = bookService.getBookById(1L);

        //Assert
        assertNotNull(actualBookDto, "The returned BookDto should not be null");
        assertEquals(expectedBookDto.getId(), actualBookDto.getId(), "The ID should match");
        assertEquals(expectedBookDto.getTitle(), actualBookDto.getTitle(), "The title should match");
        assertEquals(expectedBookDto.getAuthor(), actualBookDto.getAuthor(), "The author should match");
        assertEquals(expectedBookDto.getIsbn(), actualBookDto.getIsbn(), "The ISBN should match");
        assertEquals(expectedBookDto.getPublicationYear(), actualBookDto.getPublicationYear(), "The publication year should match");

        // Verify that findById and toDto methods were called once with correct parameters
        verify(bookRepository, times(1)).findById(1L);
        verify(bookMapper, times(1)).toDto(book);
    }

    @Test
    void testGetBookByIdThrowsBookNotFoundException() {
        //Arrange
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        //Act
        BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> {
            bookService.getBookById(1L);
        });

        //Assert
        assertEquals(exception.getMessage(), "Book with Book Id " + 1 + " Not Found!");
    }

    @Test
    void testGetBookByIdWithZeroId() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            bookService.getBookById(0L);
        });

        // Assert the exception message
        assertEquals("ID must be greater than 0", exception.getMessage());
    }

    @Test
    void testGetBookByIdWithCaching() {//How to test caching in test environment
//        // Arrange
//        Long bookId = 1L;
//
//        Book book = Book.builder()
//                .id(bookId)
//                .title("Harry Potter and the Sorcerer's Stone")
//                .author("Harry Potter")
//                .isbn("1234567891234")
//                .publicationYear(2012)
//                .build();
//
//        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
//
//        BookDto bookDto = BookDto.builder()
//                .id(bookId)
//                .title("Harry Potter and the Sorcerer's Stone")
//                .author("Harry Potter")
//                .isbn("1234567891234")
//                .publicationYear(2012)
//                .build();
//
//        when(bookMapper.toDto(any(Book.class))).thenReturn(bookDto);
//
//        // Act
//        // First call - should fetch from the repository
//        BookDto firstCallResult = bookService.getBookById(bookId);
//
//        // Second call - should fetch from the cache (repository should not be called)
//        BookDto secondCallResult = bookService.getBookById(bookId);
//
//        // Assert
//        assertEquals(bookDto, firstCallResult); // Verify the first call result
//        assertEquals(bookDto, secondCallResult); // Verify the second call result
//
//        // Verify that bookRepository.findById was called only once
//        verify(bookRepository, times(1)).findById(bookId);
    }

    @Test
    void testSaveBook() {
        // Arrange
        AddBookDto addBookDto = AddBookDto.builder()
                .title("Harry Potter and the Sorcerer's Stone")
                .author("J.K. Rowling")
                .isbn("1234567891234")
                .publicationYear(1997)
                .build();

        Book book = Book.builder()
                .title("Harry Potter and the Sorcerer's Stone")
                .author("J.K. Rowling")
                .isbn("1234567891234")
                .publicationYear(1997)
                .build();

        Book savedBook = Book.builder()
                .id(1L)
                .title("Harry Potter and the Sorcerer's Stone")
                .author("J.K. Rowling")
                .isbn("1234567891234")
                .publicationYear(1997)
                .build();

        BookDto expectedBookDto = BookDto.builder()
                .id(1L)
                .title("Harry Potter and the Sorcerer's Stone")
                .author("J.K. Rowling")
                .isbn("1234567891234")
                .publicationYear(1997)
                .build();

        // Mock behavior of bookMapper and bookRepository
        when(bookMapper.toEntity(addBookDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(savedBook);
        when(bookMapper.toDto(savedBook)).thenReturn(expectedBookDto);

        // Act
        BookDto result = bookService.saveBook(addBookDto);

        // Assert
        assertNotNull(result);
        assertEquals(expectedBookDto, result);
        verify(bookMapper, times(1)).toEntity(addBookDto);
        verify(bookRepository, times(1)).save(book);
        verify(bookMapper, times(1)).toDto(savedBook);
    }

    @Test
    void testUpdateBook_Successful() {
        // Arrange
        Long bookId = 1L;
        UpdateBookDto updateBookDto = UpdateBookDto.builder()
                .title("Updated Title")
                .author("Updated Author")
                .isbn("9876543210987")
                .publicationYear(2020)
                .build();

        Book existingBook = Book.builder().id(bookId).title("Old Title").build();
        Book updatedBook = Book.builder()
                .id(bookId)
                .title(updateBookDto.getTitle())
                .author(updateBookDto.getAuthor())
                .isbn(updateBookDto.getIsbn())
                .publicationYear(updateBookDto.getPublicationYear())
                .build();
        BookDto bookDto = BookDto.builder()
                .id(bookId)
                .title(updateBookDto.getTitle())
                .author(updateBookDto.getAuthor())
                .isbn(updateBookDto.getIsbn())
                .publicationYear(updateBookDto.getPublicationYear())
                .build();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(bookMapper.toEntity(updateBookDto)).thenReturn(updatedBook);
        when(bookRepository.save(updatedBook)).thenReturn(updatedBook);
        when(bookMapper.toDto(updatedBook)).thenReturn(bookDto);

        // Act
        BookDto result = bookService.updateBook(bookId, updateBookDto);

        // Assert
        assertNotNull(result);
        assertEquals(bookId, result.getId());
        assertEquals(updateBookDto.getTitle(), result.getTitle());
        assertEquals(updateBookDto.getAuthor(), result.getAuthor());

        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, times(1)).save(updatedBook);
    }

    @Test
    void testUpdateBook_InvalidId(){
        //Act &Assert for null ID
        IllegalArgumentException exceptionNullId = assertThrows(IllegalArgumentException.class,()->{
            bookService.updateBook(null,new UpdateBookDto());
        });
        assertEquals("ID must be greater than 0", exceptionNullId.getMessage());

        // Act & Assert for ID <= 0
        IllegalArgumentException exceptionNegativeId = assertThrows(IllegalArgumentException.class,()->{
            bookService.updateBook(0L,new UpdateBookDto());
        });
        assertEquals("ID must be greater than 0", exceptionNegativeId.getMessage());

        verify(bookRepository, never()).findById(anyLong());
        verify(bookRepository, never()).deleteById(anyLong());
    }

    @Test
    void testUpdateBook_BookNotFound(){
        //Arrange
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // Act & Assert
        BookNotFoundException bookNotFoundException = assertThrows(BookNotFoundException.class,()->{
            bookService.updateBook(bookId,new UpdateBookDto());
        });

        assertEquals("Book with Book Id 1 Not Found!", bookNotFoundException.getMessage());
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, never()).deleteById(anyLong());
    }

    @Test
    void testDeleteBook_Successful() {
        // Arrange
        Long bookId = 1L;
        Book book = Book.builder()
                .id(bookId)
                .title("Harry Potter")
                .build();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        // Act
        bookService.deleteBook(bookId);

        // Assert
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, times(1)).deleteById(bookId);
    }

    @Test
    void testDeleteBook_BookNotFound() {
        // Arrange
        Long bookId = 1L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // Act & Assert
        BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> {
            bookService.deleteBook(bookId);
        });

        assertEquals("Book with Book Id 1 Not Found!", exception.getMessage());
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, never()).deleteById(anyLong()); // Ensure delete is never called
    }

    @Test
    void testDeleteBook_InvalidId() {
        // Act & Assert for null ID
        IllegalArgumentException exceptionNullId = assertThrows(IllegalArgumentException.class, () -> {
            bookService.deleteBook(null);
        });
        assertEquals("ID must be greater than 0", exceptionNullId.getMessage());

        // Act & Assert for ID <= 0
        IllegalArgumentException exceptionNegativeId = assertThrows(IllegalArgumentException.class, () -> {
            bookService.deleteBook(0L);
        });
        assertEquals("ID must be greater than 0", exceptionNegativeId.getMessage());

        verify(bookRepository, never()).findById(anyLong());
        verify(bookRepository, never()).deleteById(anyLong());
    }

}