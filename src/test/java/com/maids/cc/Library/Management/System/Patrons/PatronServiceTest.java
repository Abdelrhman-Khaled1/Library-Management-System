package com.maids.cc.Library.Management.System.Patrons;

import com.maids.cc.Library.Management.System.Patrons.model.dto.AddPatronDto;
import com.maids.cc.Library.Management.System.Patrons.model.dto.PatronDto;
import com.maids.cc.Library.Management.System.Patrons.model.dto.UpdatePatronDto;
import com.maids.cc.Library.Management.System.Patrons.model.entity.Patron;
import com.maids.cc.Library.Management.System.Patrons.model.mapper.PatronMapper;
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
class PatronServiceTest {

    @Mock
    private PatronRepository patronRepository;

    @Mock
    private PatronMapper patronMapper;

    @InjectMocks
    private PatronService patronService;

    @Test
    void testGetAllPatrons() {
        // Arrange
        List<Patron> patrons = List.of(new Patron(), new Patron());
        when(patronRepository.findAll()).thenReturn(patrons);
        List<PatronDto> patronDtos = List.of(new PatronDto(), new PatronDto());
        when(patronMapper.toDtos(patrons)).thenReturn(patronDtos);

        // Action
        List<PatronDto> result = patronService.getAllPatrons();

        // Assertion
        assertEquals(2, result.size());
        verify(patronRepository).findAll();
        verify(patronMapper).toDtos(patrons);

        verify(patronRepository, times(1)).findAll();
        verify(patronMapper, times(1)).toDtos(patrons);
    }

    @Test
    void testGetPatronById() {
        // Arrange
        Patron patron = Patron.builder()
                .id(1L)
                .name("John Doe")
                .contactInfo("john.doe@example.com")
                .build();
        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron));
        PatronDto expectedPatronDto = PatronDto.builder()
                .id(1L)
                .name("John Doe")
                .contactInfo("john.doe@example.com")
                .build();
        when(patronMapper.toDto(any(Patron.class))).thenReturn(expectedPatronDto);

        // Act
        PatronDto actualPatronDto = patronService.getPatronById(1L);

        // Assert
        assertNotNull(actualPatronDto);
        assertEquals(expectedPatronDto.getId(), actualPatronDto.getId());
        assertEquals(expectedPatronDto.getName(), actualPatronDto.getName());
        assertEquals(expectedPatronDto.getContactInfo(), actualPatronDto.getContactInfo());

        // Verify that findById and toDto methods were called once with correct parameters
        verify(patronRepository, times(1)).findById(1L);
        verify(patronMapper, times(1)).toDto(patron);
    }

    @Test
    void testGetPatronByIdThrowsPatronNotFoundException() {
        // Arrange
        when(patronRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // Act
        PatronNotFoundException exception = assertThrows(PatronNotFoundException.class, () -> {
            patronService.getPatronById(1L);
        });

        // Assert
        assertEquals(exception.getMessage(), "Patron with Patron Id " + 1 + " Not Found!");
    }

    @Test
    void testGetPatronByIdWithZeroId() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            patronService.getPatronById(0L);
        });

        // Assert the exception message
        assertEquals("ID must be greater than 0", exception.getMessage());
    }

    @Test
    void testSavePatron() {
        // Arrange
        AddPatronDto addPatronDto = AddPatronDto.builder()
                .name("John Doe")
                .contactInfo("john.doe@example.com")
                .build();

        Patron patron = Patron.builder()
                .name("John Doe")
                .contactInfo("john.doe@example.com")
                .build();

        Patron savedPatron = Patron.builder()
                .id(1L)
                .name("John Doe")
                .contactInfo("john.doe@example.com")
                .build();

        PatronDto expectedPatronDto = PatronDto.builder()
                .id(1L)
                .name("John Doe")
                .contactInfo("john.doe@example.com")
                .build();

        // Mock behavior of patronMapper and patronRepository
        when(patronMapper.toEntity(addPatronDto)).thenReturn(patron);
        when(patronRepository.save(patron)).thenReturn(savedPatron);
        when(patronMapper.toDto(savedPatron)).thenReturn(expectedPatronDto);

        // Act
        PatronDto result = patronService.savePatron(addPatronDto);

        // Assert
        assertNotNull(result);
        assertEquals(expectedPatronDto, result);
        verify(patronMapper, times(1)).toEntity(addPatronDto);
        verify(patronRepository, times(1)).save(patron);
        verify(patronMapper, times(1)).toDto(savedPatron);
    }

    @Test
    void testUpdatePatron_Successful() {
        // Arrange
        Long patronId = 1L;
        UpdatePatronDto updatePatronDto = UpdatePatronDto.builder()
                .name("Updated Name")
                .contactInfo("updated.email@example.com")
                .build();

        Patron existingPatron = Patron.builder().id(patronId).name("Old Name").build();
        Patron updatedPatron = Patron.builder()
                .id(patronId)
                .name(updatePatronDto.getName())
                .contactInfo(updatePatronDto.getContactInfo())
                .build();
        PatronDto patronDto = PatronDto.builder()
                .id(patronId)
                .name(updatePatronDto.getName())
                .contactInfo(updatePatronDto.getContactInfo())
                .build();

        when(patronRepository.findById(patronId)).thenReturn(Optional.of(existingPatron));
        when(patronMapper.toEntity(updatePatronDto)).thenReturn(updatedPatron);
        when(patronRepository.save(updatedPatron)).thenReturn(updatedPatron);
        when(patronMapper.toDto(updatedPatron)).thenReturn(patronDto);

        // Act
        PatronDto result = patronService.updatePatron(patronId, updatePatronDto);

        // Assert
        assertNotNull(result);
        assertEquals(patronId, result.getId());
        assertEquals(updatePatronDto.getName(), result.getName());
        assertEquals(updatePatronDto.getContactInfo(), result.getContactInfo());

        verify(patronRepository, times(1)).findById(patronId);
        verify(patronRepository, times(1)).save(updatedPatron);
    }

    @Test
    void testUpdatePatron_InvalidId() {
        // Act & Assert for null ID
        IllegalArgumentException exceptionNullId = assertThrows(IllegalArgumentException.class, () -> {
            patronService.updatePatron(null, new UpdatePatronDto());
        });
        assertEquals("ID must be greater than 0", exceptionNullId.getMessage());

        // Act & Assert for ID <= 0
        IllegalArgumentException exceptionNegativeId = assertThrows(IllegalArgumentException.class, () -> {
            patronService.updatePatron(0L, new UpdatePatronDto());
        });
        assertEquals("ID must be greater than 0", exceptionNegativeId.getMessage());

        verify(patronRepository, never()).findById(anyLong());
        verify(patronRepository, never()).save(any(Patron.class));
    }

    @Test
    void testUpdatePatron_PatronNotFound() {
        // Arrange
        Long patronId = 1L;
        when(patronRepository.findById(patronId)).thenReturn(Optional.empty());

        // Act & Assert
        PatronNotFoundException patronNotFoundException = assertThrows(PatronNotFoundException.class, () -> {
            patronService.updatePatron(patronId, new UpdatePatronDto());
        });

        assertEquals("Patron with Patron Id " + patronId + " Not Found!", patronNotFoundException.getMessage());
        verify(patronRepository, times(1)).findById(patronId);
        verify(patronRepository, never()).save(any(Patron.class));
    }

    @Test
    void testDeletePatron_Successful() {
        // Arrange
        Long patronId = 1L;
        Patron patron = Patron.builder()
                .id(patronId)
                .name("John Doe")
                .build();

        when(patronRepository.findById(patronId)).thenReturn(Optional.of(patron));

        // Act
        patronService.deletePatron(patronId);

        // Assert
        verify(patronRepository, times(1)).findById(patronId);
        verify(patronRepository, times(1)).deleteById(patronId);
    }

    @Test
    void testDeletePatron_PatronNotFound() {
        // Arrange
        Long patronId = 1L;

        when(patronRepository.findById(patronId)).thenReturn(Optional.empty());

        // Act & Assert
        PatronNotFoundException exception = assertThrows(PatronNotFoundException.class, () -> {
            patronService.deletePatron(patronId);
        });

        assertEquals("Patron with Patron Id " + patronId + " Not Found!", exception.getMessage());
        verify(patronRepository, times(1)).findById(patronId);
        verify(patronRepository, never()).deleteById(patronId);
    }

    @Test
    void testDeleteBook_InvalidId() {
        // Act & Assert for null ID
        IllegalArgumentException exceptionNullId = assertThrows(IllegalArgumentException.class, () -> {
            patronService.deletePatron(null);
        });
        assertEquals("ID must be greater than 0", exceptionNullId.getMessage());

        // Act & Assert for ID <= 0
        IllegalArgumentException exceptionNegativeId = assertThrows(IllegalArgumentException.class, () -> {
            patronService.deletePatron(0L);
        });
        assertEquals("ID must be greater than 0", exceptionNegativeId.getMessage());

        verify(patronRepository, never()).findById(anyLong());
        verify(patronRepository, never()).deleteById(anyLong());
    }
}
