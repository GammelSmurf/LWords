package org.example.authentication.Controllers;

import org.example.authentication.Exceprions.UserNotFoundException;
import org.example.authentication.Exceprions.WrongPasswordException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AdviceController {
    @ExceptionHandler({UserNotFoundException.class, WrongPasswordException.class})
    public ResponseEntity<Object> handleException(Exception ex) {
        return ResponseEntity
                .badRequest()
                .body(ex.getMessage());
    }
}
