package com.maids.cc.Library.Management.System.Patrons;

import com.maids.cc.Library.Management.System.Patrons.model.dto.AddPatronDto;
import com.maids.cc.Library.Management.System.Patrons.model.dto.PatronDto;
import com.maids.cc.Library.Management.System.Patrons.model.dto.UpdatePatronDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/patrons")
@RequiredArgsConstructor
public class PatronController {

    private final PatronService patronService;

    @GetMapping
    public List<PatronDto> getAllPatrons() {
        return patronService.getAllPatrons();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatronDto> getPatronById(@PathVariable Long id) {
        PatronDto patron = patronService.getPatronById(id);
        return ResponseEntity.status(HttpStatus.OK).body(patron);
    }

    @PostMapping
    public ResponseEntity<PatronDto> addPatron(@Valid @RequestBody AddPatronDto patronDto) {
        PatronDto savedPatron = patronService.savePatron(patronDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPatron);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatronDto> updatePatron(@PathVariable Long id, @Valid @RequestBody UpdatePatronDto updatedPatronDto) {
        PatronDto updated = patronService.updatePatron(id, updatedPatronDto);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatron(@PathVariable Long id) {
        patronService.deletePatron(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
