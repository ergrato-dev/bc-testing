package com.bootcamp.web;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Cada excepción del dominio, a un código HTTP con un cuerpo ProblemDetail (RFC 9457).
 * ResponseEntityExceptionHandler ya responde 400 cuando falla @Valid o el JSON no se puede leer.
 */
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    // TODO: un método @ExceptionHandler por cada excepción de tu dominio (paso 4 del README).
    //
    // @ExceptionHandler(ValidationException.class)
    // ProblemDetail rejected(ValidationException e) {
    //     return ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_CONTENT, e.getMessage());
    // }
}
