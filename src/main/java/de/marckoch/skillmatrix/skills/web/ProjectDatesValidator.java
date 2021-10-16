package de.marckoch.skillmatrix.skills.web;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;

// https://linuxtut.com/en/eb3bf3b5301bae398cc2/
public class ProjectDatesValidator implements ConstraintValidator<ProjectDatesValidation, ProjectDTO> {
    @Override
    public boolean isValid(ProjectDTO value, ConstraintValidatorContext context) {
        if (value == null || value.getSince() == null || value.getUntil() == null)
            return true;
        if (value.getSince().isEmpty() || value.getUntil().isEmpty())
            return true;

        try {
            YearMonth since = YearMonth.parse(value.getSince());
            YearMonth until = YearMonth.parse(value.getUntil());
            return (since.isBefore(until));
        } catch (DateTimeParseException dtpEx) {
            return true;
        }
    }
}
