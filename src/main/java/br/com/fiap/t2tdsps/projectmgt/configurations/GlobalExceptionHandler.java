package br.com.fiap.t2tdsps.projectmgt.configurations;

import br.com.fiap.t2tdsps.projectmgt.dto.ErrorCustomResponse;
import br.com.fiap.t2tdsps.projectmgt.exceptions.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorCustomResponse> handleException(ResourceNotFoundException ex){
        return ResponseEntity
                .status(404) // status do response entity -> status response
                .body(
                        new ErrorCustomResponse(LocalDateTime.now(),404,"Not found",ex.getMessage()) // body do response entity -> corpo response
                );
    }

    //Desafio: Crie um handler para evitar que errors 500 seja retornados crus ao cliente
}
