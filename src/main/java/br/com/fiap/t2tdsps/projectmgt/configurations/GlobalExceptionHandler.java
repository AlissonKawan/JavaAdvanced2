package br.com.fiap.t2tdsps.projectmgt.configurations;

import br.com.fiap.t2tdsps.projectmgt.dto.ErrorCustomResponse;
import br.com.fiap.t2tdsps.projectmgt.dto.ErrorFieldCustom;
import br.com.fiap.t2tdsps.projectmgt.dto.ValidationErrorResponse;
import br.com.fiap.t2tdsps.projectmgt.exceptions.InvalidStatusTransitionException;
import br.com.fiap.t2tdsps.projectmgt.exceptions.MilestoneAlreadyCompletedException;
import br.com.fiap.t2tdsps.projectmgt.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorCustomResponse> handleException(ResourceNotFoundException ex){
        return build(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(InvalidStatusTransitionException.class)
    public ResponseEntity<ErrorCustomResponse> handleInvalidStatusTransition(InvalidStatusTransitionException ex) {
        return build(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(MilestoneAlreadyCompletedException.class)
    public ResponseEntity<ErrorCustomResponse> handleMilestoneAlreadyCompleted(MilestoneAlreadyCompletedException ex) {
        return build(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorCustomResponse> handleNotReadable(HttpMessageNotReadableException ex) {
        return build(HttpStatus.BAD_REQUEST, "Corpo da requisição inválido ou malformado");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorCustomResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return build(HttpStatus.BAD_REQUEST, "Parâmetro '" + ex.getName() + "' inválido: " + ex.getValue());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorCustomResponse> handleIllegalArgument(IllegalArgumentException ex) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage());
    }


    //Exerc: implementar um exception handler para MethodArgumentNotValidException
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleArgumentNotValidException(MethodArgumentNotValidException ex){

        List<ErrorFieldCustom> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> new ErrorFieldCustom(
                        fieldError.getField(),
                        fieldError.getDefaultMessage()
                ))
                .toList();

        return ResponseEntity.badRequest().body(new ValidationErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), errors));
    }


    // Desafio resolvido: sem esse handler, qualquer erro não previsto (bug, NullPointerException etc.)
    // vazava cru pro cliente como HTML de erro do Tomcat/Spring - com stack trace incluso. Esse catch-all
    // garante que TODO erro da API sai no mesmo formato ErrorCustomResponse, mesmo os que a gente não previu.
//    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorCustomResponse> handleGeneric(Exception ex) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno inesperado");
    }

    private ResponseEntity<ErrorCustomResponse> build(HttpStatus status, String message) {
        return ResponseEntity
                .status(status)
                .body(new ErrorCustomResponse(LocalDateTime.now(), status.value(), status.getReasonPhrase(), message));
    }
}
