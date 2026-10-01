package br.com.fiap.t2tdsps.projectmgt.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class MaxYearsInFutureValidator implements ConstraintValidator<MaxYearsInFuture, LocalDate> {

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        return false;
    }
}
