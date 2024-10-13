package com.maids.cc.Library.Management.System.Patrons;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Patron not found")
public class PatronNotFoundException extends RuntimeException {
    public PatronNotFoundException(Long patronId) {
        super("Patron with Patron Id " + patronId + " Not Found!");
    }
}
