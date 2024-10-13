package com.maids.cc.Library.Management.System.Patrons.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddPatronDto {

    @Size(max = 30, message = "Patron's name must not exceed 30 characters")
    @Size(min = 3, message = "Patron's name must be at least 3 characters long")
    private String name;

    @NotBlank(message = "Contact Information must not be blank")
    private String contactInfo;
    private String notes;
}
