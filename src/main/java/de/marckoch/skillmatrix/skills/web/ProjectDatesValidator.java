package de.marckoch.skillmatrix.skills.web;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

// https://linuxtut.com/en/eb3bf3b5301bae398cc2/
public class ProjectDatesValidator implements ConstraintValidator<ProjectDatesValidation, ProjectDTO> {
    @Override
    public boolean isValid(ProjectDTO value, ConstraintValidatorContext context) {
        if (value == null || value.getSince() == null || value.getUntil() == null)
            return true;
        return (value.getSince().isBefore(value.getUntil()));
    }
}
