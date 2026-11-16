package com.bootcamp;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** Convierte LoanNotFoundException en un 404 con cuerpo ProblemDetail. */
@RestControllerAdvice
public class LibraryExceptionHandler {

    @ExceptionHandler(LoanNotFoundException.class)
    ProblemDetail notFound(LoanNotFoundException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }
}
