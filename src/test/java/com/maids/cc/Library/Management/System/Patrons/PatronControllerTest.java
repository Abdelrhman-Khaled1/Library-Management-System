package com.maids.cc.Library.Management.System.Patrons;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maids.cc.Library.Management.System.Patrons.model.dto.AddPatronDto;
import com.maids.cc.Library.Management.System.Patrons.model.dto.PatronDto;
import com.maids.cc.Library.Management.System.Patrons.model.dto.UpdatePatronDto;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc(addFilters = false)
class PatronControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatronService patronService;

    @Autowired
    private ObjectMapper objectMapper;  // To convert objects to JSON

    @Test
    void testGetPatronById_Success() throws Exception {
        // Arrange
        Long patronId = 1L;
        PatronDto patronDto = PatronDto.builder()
                .id(patronId)
                .name("John Doe")
                .contactInfo("john.doe@example.com")
                .build();

        when(patronService.getPatronById(patronId)).thenReturn(patronDto);

        // Act & Assert
        mockMvc.perform(get("/api/v1/patrons/{id}", patronId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{'id': 1, 'name': 'John Doe', 'contactInfo': 'john.doe@example.com'}"));
    }

    @Test
    void testGetPatronById_NotFound() throws Exception {
        // Arrange
        Long patronId = 1L;

        when(patronService.getPatronById(patronId)).thenThrow(new PatronNotFoundException(patronId));

        // Act & Assert
        mockMvc.perform(get("/api/v1/patrons/{id}", patronId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof PatronNotFoundException))
                .andExpect(result -> assertEquals("Patron with Patron Id " + patronId + " Not Found!", result.getResolvedException().getMessage()));
    }

    @Test
    void testGetPatronById_NegativeId() throws Exception {
        // Arrange
        Long negativeId = -1L;

        when(patronService.getPatronById(negativeId)).thenThrow(new IllegalArgumentException("ID must be greater than 0"));

        // Act & Assert
        mockMvc.perform(get("/api/v1/patrons/{id}", negativeId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result -> assertEquals("ID must be greater than 0", result.getResolvedException().getMessage()));
    }

    @Test
    void testGetAllPatrons() throws Exception {
        // Arrange
        PatronDto patron1 = PatronDto.builder()
                .id(1L)
                .name("John Doe")
                .contactInfo("john.doe@example.com")
                .build();

        PatronDto patron2 = PatronDto.builder()
                .id(2L)
                .name("Jane Smith")
                .contactInfo("jane.smith@example.com")
                .build();

        List<PatronDto> patrons = Arrays.asList(patron1, patron2);
        when(patronService.getAllPatrons()).thenReturn(patrons);

        // Act & Assert
        mockMvc.perform(get("/api/v1/patrons")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))  // Check that there are 2 patrons in the response
                .andExpect(jsonPath("$[0].name", is("John Doe")))  // Validate first patron details
                .andExpect(jsonPath("$[1].name", is("Jane Smith")));  // Validate second patron details
    }

    @Test
    void testAddPatron_Success() throws Exception {
        // Arrange
        AddPatronDto newPatron = AddPatronDto.builder()
                .name("Alice Johnson")
                .contactInfo("alice.johnson@example.com")
                .build();

        PatronDto savedPatron = PatronDto.builder()
                .id(1L)
                .name("Alice Johnson")
                .contactInfo("alice.johnson@example.com")
                .build();

        when(patronService.savePatron(Mockito.any(AddPatronDto.class))).thenReturn(savedPatron);

        // Act & Assert
        mockMvc.perform(post("/api/v1/patrons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPatron))  // Convert AddPatronDto to JSON
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())  // Expect HTTP 201 Created
                .andExpect(jsonPath("$.id", is(1)))  // Validate the saved patron's id
                .andExpect(jsonPath("$.name", is("Alice Johnson")))  // Validate the name
                .andExpect(jsonPath("$.contactInfo", is("alice.johnson@example.com")));  // Validate the contact info
    }

    @Test
    void testAddPatron_InvalidDto() throws Exception {
        // Arrange: Create an invalid AddPatronDto object with incorrect values
        AddPatronDto invalidPatron = AddPatronDto.builder()
                .name("Ab")  // Invalid: name is blank
                .contactInfo("")
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/v1/patrons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidPatron))  // Convert invalidPatron to JSON
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())  // Expect HTTP 400 Bad Request
                .andExpect(jsonPath("$.errors[?(@.field == 'name')].message", hasItem("Patron's name must be at least 3 characters long")))
                .andExpect(jsonPath("$.errors[?(@.field == 'contactInfo')].message", hasItem("Contact Information must not be blank")));
    }

    @Test
    void testUpdatePatron_Success() throws Exception {
        Long patronId = 1L;
        UpdatePatronDto updatePatronDto = new UpdatePatronDto();
        // Ensure the DTO has valid values
        updatePatronDto.setName("Updated Name");
        updatePatronDto.setContactInfo("updated.email@example.com");

        PatronDto updatedPatronDto = new PatronDto();
        updatedPatronDto.setId(patronId);
        updatedPatronDto.setName("Updated Name");
        updatedPatronDto.setContactInfo("updated.email@example.com");

        when(patronService.updatePatron(patronId, updatePatronDto)).thenReturn(updatedPatronDto);

        mockMvc.perform(put("/api/v1/patrons/{id}", patronId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatePatronDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updatedPatronDto)));
    }

    @Test
    void testUpdatePatron_InvalidUpdatePatronDto() throws Exception {
        // Create an UpdatePatronDto with invalid data
        UpdatePatronDto updatePatronDto = new UpdatePatronDto();
        updatePatronDto.setName("A"); // Invalid: Name size should be more than 2
        updatePatronDto.setContactInfo(""); // Invalid: Not a valid contact information format

        mockMvc.perform(put("/api/v1/patrons/{id}", 1L) // Assume patron ID is 1
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatePatronDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()) // Expect HTTP 400 Bad Request
                .andExpect(jsonPath("$.errors[*].field", containsInAnyOrder("name", "contactInfo")))
                .andExpect(jsonPath("$.errors[*].message", containsInAnyOrder(
                        "Patron's name must be at least 3 characters long",
                        "Contact Information must not be blank")));
    }

    @Test
    void testUpdatePatron_IdLessThanZero() throws Exception {
        // Invalid ID
        Long invalidId = 0L;

        // Create an instance of UpdatePatronDto with valid data
        UpdatePatronDto updatePatronDto = new UpdatePatronDto();
        updatePatronDto.setName("Valid Name");
        updatePatronDto.setContactInfo("valid.contact@example.com"); // Using contactInfo instead of email

        // Expect an IllegalArgumentException to be thrown
        doThrow(new IllegalArgumentException("ID must be greater than 0"))
                .when(patronService).updatePatron(invalidId, updatePatronDto);

        mockMvc.perform(put("/api/v1/patrons/{id}", invalidId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatePatronDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result -> assertEquals("ID must be greater than 0", result.getResolvedException().getMessage()));
    }

    @Test
    void testUpdatePatron_NotFound() throws Exception {
        Long patronId = 1L;

        // Create an instance of UpdatePatronDto with valid data
        UpdatePatronDto updatePatronDto = new UpdatePatronDto();
        updatePatronDto.setName("Valid Name");
        updatePatronDto.setContactInfo("valid.contact@example.com"); // Using contactInfo instead of email

        // Patron not found
        doThrow(new PatronNotFoundException(patronId)).when(patronService).updatePatron(patronId, updatePatronDto);

        mockMvc.perform(put("/api/v1/patrons/{id}", patronId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatePatronDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof PatronNotFoundException))
                .andExpect(result -> assertEquals("Patron with Patron Id " + patronId + " Not Found!", result.getResolvedException().getMessage()));
    }


    @Test
    void testDeletePatron_Success() throws Exception {
        Long patronId = 1L;

        mockMvc.perform(delete("/api/v1/patrons/{id}", patronId))
                .andExpect(status().isNoContent()); // Expect HTTP 204 No Content

        verify(patronService, times(1)).deletePatron(patronId); // Verify the service method was called once
    }

    @Test
    void testDeletePatron_NotFound() throws Exception {
        Long patronId = 1L;

        doThrow(new PatronNotFoundException(patronId)).when(patronService).deletePatron(patronId);

        mockMvc.perform(delete("/api/v1/patrons/{id}", patronId))
                .andExpect(status().isNotFound()) // Expect HTTP 404 Not Found
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof PatronNotFoundException))
                .andExpect(result -> assertEquals("Patron with Patron Id " + patronId + " Not Found!", result.getResolvedException().getMessage()));
    }

    @Test
    void testDeletePatron_IdLessThanZero() throws Exception {
        // Invalid ID
        Long invalidId = 0L;

        doThrow(new IllegalArgumentException("ID must be greater than 0"))
                .when(patronService).deletePatron(invalidId);

        mockMvc.perform(delete("/api/v1/patrons/{id}", invalidId))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result -> assertEquals("ID must be greater than 0", result.getResolvedException().getMessage()));
    }
}
