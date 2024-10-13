package com.maids.cc.Library.Management.System.Books.model.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddBookDto {

    @NotBlank(message = "Title should not be null")
    @Size(max = 100, message = "Title's size should not be more than 100 characters")
    @Size(min = 3, message = "Title's size should not be less than 3 characters")
    private String title;

    @Size(min=3, message = "Author's name must be at least 3 characters long")
    @Size(max=30, message = "Author's name must not exceed 30 characters")
    private String author;

    @Min(value = 1450, message = "Publication year should not be before 1450")
    @Max(value = 2024, message = "Publication year should not be in the future")
    private Integer publicationYear;

    @Pattern(regexp = "^(?:\\d{9}[\\dXx]|\\d{13})$", message = "Invalid ISBN format. Must be ISBN-10 or ISBN-13")
    private String isbn;
}
