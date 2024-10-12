package com.maids.cc.Library.Management.System.Books.model.mapper;

import com.maids.cc.Library.Management.System.Books.model.dto.AddBookDto;
import com.maids.cc.Library.Management.System.Books.model.dto.BookDto;
import com.maids.cc.Library.Management.System.Books.model.dto.UpdateBookDto;
import com.maids.cc.Library.Management.System.Books.model.entity.Book;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {

    Book toEntity(AddBookDto dto);
    Book toEntity(UpdateBookDto dto);
    List<BookDto> toDtos(List<Book> entities);
    BookDto toDto (Book book);

}
