package com.maids.cc.Library.Management.System.Books;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maids.cc.Library.Management.System.Books.model.dto.AddBookDto;
import com.maids.cc.Library.Management.System.Books.model.dto.BookDto;
import com.maids.cc.Library.Management.System.Books.model.dto.UpdateBookDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;


@WebMvcTest(controllers = BookController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookService bookService;
    @Autowired
    private ObjectMapper objectMapper;  // To convert objects to JSON


    @Test
    void testGetBookById_Success() throws Exception {
        // Arrange
        Long bookId = 1L;
        BookDto bookDto = BookDto.builder()
                .id(bookId)
                .title("Harry Potter and the Sorcerer's Stone")
                .author("J.K. Rowling")
                .isbn("1234567891234")
                .publicationYear(1997)
                .build();

        when(bookService.getBookById(bookId)).thenReturn(bookDto);

        // Act & Assert
        mockMvc.perform(get("/api/v1/books/{id}", bookId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{'id': 1, 'title': 'Harry Potter and the Sorcerer\\'s Stone', 'author': 'J.K. Rowling', 'isbn': '1234567891234', 'publicationYear': 1997}"));
    }

    @Test
    void testGetBookById_NotFound() throws Exception {
        // Arrange
        Long bookId = 1L;

        when(bookService.getBookById(bookId)).thenThrow(new BookNotFoundException(bookId));

        // Act & Assert
        mockMvc.perform(get("/api/v1/books/{id}", bookId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BookNotFoundException))
                .andExpect(result -> assertEquals("Book with Book Id "+bookId+ " Not Found!", result.getResolvedException().getMessage()));
    }

    @Test
    void testGetBookById_NegativeId() throws Exception {
        // Arrange
        Long negativeId = -1L;

        when(bookService.getBookById(negativeId)).thenThrow(new IllegalArgumentException("ID must be greater than 0"));

        // Act & Assert
        mockMvc.perform(get("/api/v1/books/{id}", negativeId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result -> assertEquals("ID must be greater than 0", result.getResolvedException().getMessage()));
    }

    @Test
    void testGetBookById_NullId() throws Exception {
        //Arrange
//        when(bookService.getBookById(null)).thenThrow(new IllegalArgumentException("ID must be greater than 0"));0
//        // Act & Assert
//        mockMvc.perform(get("/api/v1/books/{id}", "null")
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest()) // here I have a problem I
//                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
//                .andExpect(result -> assertEquals("ID must be greater than 0", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void testGetAllBooks() throws Exception {
        // Arrange
        BookDto book1 = BookDto.builder()
                .id(1L)
                .title("Harry Potter and the Sorcerer's Stone")
                .author("J.K. Rowling")
                .isbn("1234567891234")
                .publicationYear(1997)
                .build();

        BookDto book2 = BookDto.builder()
                .id(2L)
                .title("The Hobbit")
                .author("J.R.R. Tolkien")
                .isbn("1234567895678")
                .publicationYear(1937)
                .build();

        List<BookDto> books = Arrays.asList(book1, book2);
        when(bookService.getAllBooks()).thenReturn(books);

        // Act & Assert
        mockMvc.perform(get("/api/v1/books")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))  // Check that there are 2 books in the response
                .andExpect(jsonPath("$[0].title", is("Harry Potter and the Sorcerer's Stone")))  // Validate first book details
                .andExpect(jsonPath("$[1].title", is("The Hobbit")));  // Validate second book details
    }

    @Test
    void testAddBook_Success() throws Exception {
        // Arrange
        AddBookDto newBook = AddBookDto.builder()
                .title("The Great Gatsby")
                .author("F. Scott Fitzgerald")
                .isbn("9780743273565")
                .publicationYear(1925)
                .build();

        BookDto savedBook = BookDto.builder()
                .id(1L)
                .title("The Great Gatsby")
                .author("F. Scott Fitzgerald")
                .isbn("9780743273565")
                .publicationYear(1925)
                .build();

        when(bookService.saveBook(Mockito.any(AddBookDto.class))).thenReturn(savedBook);

        // Act & Assert
        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newBook))  // Convert AddBookDto to JSON
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())  // Expect HTTP 201 Created
                .andExpect(jsonPath("$.id", is(1)))  // Validate the saved book's id
                .andExpect(jsonPath("$.title", is("The Great Gatsby")))  // Validate the title
                .andExpect(jsonPath("$.author", is("F. Scott Fitzgerald")))  // Validate the author
                .andExpect(jsonPath("$.isbn", is("9780743273565")))  // Validate the ISBN
                .andExpect(jsonPath("$.publicationYear", is(1925)));  // Validate the publication year
    }

    @Test
    void testAddBook_InvalidDto() throws Exception {
        // Arrange: Create an invalid AddBookDto object with incorrect values
        AddBookDto invalidBook = AddBookDto.builder()
                .title("")  // Invalid: title is blank
                .author("AB")  // Invalid: author's name is less than 3 characters
                .publicationYear(1400)  // Invalid: publication year is before 1450
                .isbn("invalidISBN")  // Invalid: wrong ISBN format
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidBook))  // Convert invalidBook to JSON
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())  // Expect HTTP 400 Bad Request
                .andExpect(jsonPath("$.errors[?(@.field == 'title')].message", hasItem("Title should not be null")))  // Validate error message for 'title'
                .andExpect(jsonPath("$.errors[?(@.field == 'author')].message", hasItem("Author's name must be at least 3 characters long")))  // Validate error message for 'author'
                .andExpect(jsonPath("$.errors[?(@.field == 'publicationYear')].message", hasItem("Publication year should not be before 1450")))  // Validate error message for 'publicationYear'
                .andExpect(jsonPath("$.errors[?(@.field == 'isbn')].message", hasItem("Invalid ISBN format. Must be ISBN-10 or ISBN-13")));  // Validate error message for 'isbn'
    }

    @Test
    void testUpdateBook_Success() throws Exception {
        Long bookId = 1L;
        UpdateBookDto updateBookDto = new UpdateBookDto();
        // Ensure the DTO has valid values
        updateBookDto.setTitle("Valid Title");
        updateBookDto.setAuthor("Valid Author");
        updateBookDto.setPublicationYear(2020);
        updateBookDto.setIsbn("1234567890"); // Example ISBN

        BookDto updatedBookDto = new BookDto();
        updatedBookDto.setTitle("Updated Title");

        when(bookService.updateBook(bookId, updateBookDto)).thenReturn(updatedBookDto);

        mockMvc.perform(put("/api/v1/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateBookDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(updatedBookDto)));
    }

    @Test
    void testUpdateBook_InvalidUpdateBookDto() throws Exception {
        // Create an UpdateBookDto with invalid data
        UpdateBookDto updateBookDto = new UpdateBookDto();
        updateBookDto.setTitle(""); // Invalid: Title should not be blank
        updateBookDto.setAuthor("Au"); // Invalid: Author's name must be at least 3 characters long
        updateBookDto.setPublicationYear(1400); // Invalid: Year should not be before 1450
        updateBookDto.setIsbn("InvalidISBN"); // Invalid: Not a valid ISBN format

        mockMvc.perform(put("/api/v1/books/{id}", 1L) // Assume book ID is 1
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateBookDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()) // Expect HTTP 400 Bad Request
                .andExpect(jsonPath("$.errors", hasSize(4))) // Validate that 4 validation errors are returned
                .andExpect(jsonPath("$.errors[*].field", containsInAnyOrder("title", "author", "publicationYear", "isbn"))) // Check all fields are present
                .andExpect(jsonPath("$.errors[*].message", containsInAnyOrder(
                        "Title should not be null",
                        "Author's name must be at least 3 characters long",
                        "Publication year should not be before 1450",
                        "Invalid ISBN format. Must be ISBN-10 or ISBN-13"))); // Check all error messages are present
    }

    @Test
    void testUpdateBook_IdLessThanZero() throws Exception {
        // Invalid ID
        Long invalidId = 0L;

        // Create an instance of UpdateBookDto with valid data
        UpdateBookDto updateBookDto = new UpdateBookDto();
        updateBookDto.setTitle("Valid Title");
        updateBookDto.setAuthor("Valid Author");
        updateBookDto.setPublicationYear(2020);
        updateBookDto.setIsbn("123456789X"); // or any valid ISBN

        // Expect an IllegalArgumentException to be thrown
        doThrow(new IllegalArgumentException("ID must be greater than 0"))
                .when(bookService).updateBook(invalidId, updateBookDto);

        mockMvc.perform(put("/api/v1/books/{id}", invalidId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateBookDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result -> assertEquals("ID must be greater than 0", result.getResolvedException().getMessage()));
    }

    @Test
    void testUpdateBook_BookNotFound() throws Exception {
        Long bookId = 1L;

        // Create an instance of UpdateBookDto with valid data
        UpdateBookDto updateBookDto = new UpdateBookDto();
        updateBookDto.setTitle("Valid Title");
        updateBookDto.setAuthor("Valid Author");
        updateBookDto.setPublicationYear(2020);
        updateBookDto.setIsbn("123456789X"); // or any valid ISBN

        // Book not found
        doThrow(new BookNotFoundException(bookId)).when(bookService).updateBook(bookId, updateBookDto);

        mockMvc.perform(put("/api/v1/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateBookDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteBook_Success() throws Exception {
        Long bookId = 1L;

        mockMvc.perform(delete("/api/v1/books/{id}", bookId))
                .andExpect(status().isNoContent());

        verify(bookService, times(1)).deleteBook(bookId);
    }

    @Test
    void testDeleteBook_IdLessThanZero() throws Exception {
        // Invalid ID
        Long invalidId = 0L;

        doThrow(new IllegalArgumentException("ID must be greater than 0"))
                .when(bookService).deleteBook(invalidId);

        mockMvc.perform(delete("/api/v1/books/{id}", invalidId))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result -> assertEquals("ID must be greater than 0", result.getResolvedException().getMessage()));
    }

    @Test
    void testDeleteBook_BookNotFound() throws Exception {
        Long bookId = 1L;

        // Book not found
        doThrow(new BookNotFoundException(bookId)).when(bookService).deleteBook(bookId);

        mockMvc.perform(delete("/api/v1/books/{id}", bookId))
                .andExpect(status().isNotFound());
    }

}