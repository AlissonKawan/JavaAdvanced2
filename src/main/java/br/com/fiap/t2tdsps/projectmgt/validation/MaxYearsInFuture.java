package br.com.fiap.t2tdsps.projectmgt.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = {MaxYearsInFutureValidator.class})
@Target({FIELD})
@Retention(RUNTIME)
public @interface MaxYearsInFuture {
    String message() default "dueDate não pode estar a mais de 2 anos no futuro";
    int value();
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
