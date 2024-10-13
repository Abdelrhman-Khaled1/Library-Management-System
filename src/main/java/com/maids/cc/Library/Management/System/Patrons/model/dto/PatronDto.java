package com.maids.cc.Library.Management.System.Patrons.model.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatronDto {
    private Long id;
    private String name;
    private String contactInfo;
    private String notes;
}
