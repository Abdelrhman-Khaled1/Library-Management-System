package com.maids.cc.Library.Management.System.Patrons;

import com.maids.cc.Library.Management.System.Patrons.model.dto.AddPatronDto;
import com.maids.cc.Library.Management.System.Patrons.model.dto.PatronDto;
import com.maids.cc.Library.Management.System.Patrons.model.dto.UpdatePatronDto;
import com.maids.cc.Library.Management.System.Patrons.model.entity.Patron;
import com.maids.cc.Library.Management.System.Patrons.model.mapper.PatronMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatronService {
    private final PatronRepository patronRepository;
    private final PatronMapper patronMapper;

    public List<PatronDto> getAllPatrons() {
        return patronMapper.toDtos(patronRepository.findAll());
    }

    @Cacheable(value = "patrons", key = "#id", unless = "#result == null")
    public PatronDto getPatronById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID must be greater than 0");
        }

        Optional<Patron> patron = patronRepository.findById(id);
        if (patron.isPresent()) {
            return patronMapper.toDto(patron.get());
        } else {
            throw new PatronNotFoundException(id);
        }
    }

    public PatronDto savePatron(AddPatronDto patronDto) {
        Patron addPatron = patronMapper.toEntity(patronDto);
        Patron savedPatron = patronRepository.save(addPatron);
        return patronMapper.toDto(savedPatron);
    }

    @CachePut(value = "patrons", key = "#id")
    public PatronDto updatePatron(Long id, UpdatePatronDto updatedPatronDto) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID must be greater than 0");
        }
        if (patronRepository.findById(id).isPresent()) {
            Patron updatedPatron = patronMapper.toEntity(updatedPatronDto);
            updatedPatron.setId(id);
            Patron patron = patronRepository.save(updatedPatron);
            return patronMapper.toDto(patron);
        }
        throw new PatronNotFoundException(id);
    }

    @CacheEvict(value = "patrons", key = "#id")
    public void deletePatron(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID must be greater than 0");
        }

        if (patronRepository.findById(id).isPresent()) {
            patronRepository.deleteById(id);
        } else {
            throw new PatronNotFoundException(id);
        }
    }
}
