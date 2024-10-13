package com.maids.cc.Library.Management.System.Patrons.model.mapper;

import com.maids.cc.Library.Management.System.Patrons.model.dto.AddPatronDto;
import com.maids.cc.Library.Management.System.Patrons.model.dto.PatronDto;
import com.maids.cc.Library.Management.System.Patrons.model.dto.UpdatePatronDto;
import com.maids.cc.Library.Management.System.Patrons.model.entity.Patron;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PatronMapper {
    Patron toEntity(AddPatronDto dto);
    Patron toEntity(UpdatePatronDto dto);
    List<PatronDto> toDtos(List<Patron> entities);
    PatronDto toDto(Patron patron);
}
