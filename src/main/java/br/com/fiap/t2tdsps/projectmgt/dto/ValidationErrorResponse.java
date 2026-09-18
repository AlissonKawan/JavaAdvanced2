package br.com.fiap.t2tdsps.projectmgt.dto;

import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public record ValidationErrorResponse(LocalDateTime timestamp, int status, String error, List<FieldError> errors) {
}
