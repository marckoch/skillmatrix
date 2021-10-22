package de.marckoch.skillmatrix.skills.web.dto;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ProjectDatesValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ProjectDatesValidation {
    String message() default "'since' must be before 'until'";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
