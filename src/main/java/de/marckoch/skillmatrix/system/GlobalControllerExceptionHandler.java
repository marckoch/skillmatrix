package de.marckoch.skillmatrix.system;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalControllerExceptionHandler {
    // Convert a predefined exception to an HTTP Status code
    @ResponseStatus(value= HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public void noSuchElement() {
        // Nothing to do
    }
}