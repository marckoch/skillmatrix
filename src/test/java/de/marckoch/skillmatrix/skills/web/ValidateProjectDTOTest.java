package de.marckoch.skillmatrix.skills.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.YearMonth;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

public class ValidateProjectDTOTest {

    protected Validator validator;

    @BeforeEach
    public void setUp() {
        validator = Validation
                .buildDefaultValidatorFactory()
                .getValidator();
    }

    @Test
    void testSinceBeforeUntilViolationIsDetected() {
        ProjectDTO p = ProjectDTO.builder()
                .name("test")
                .since(YearMonth.of(2020,3))
                .until(YearMonth.of(2014,4))
                .build();

        Set<ConstraintViolation<ProjectDTO>> violations = validator.validate(p);

        assertThat(violations).hasSize(1);
        violations.forEach(v -> assertThat(v.getMessage()).contains("'since' must be before 'until'"));
    }

    @Test
    void testNoViolationWhenSinceIsBeforeUntil() {
        ProjectDTO p = ProjectDTO.builder()
                .name("test")
                .since(YearMonth.of(2020,3))
                .until(YearMonth.of(2021,4))
                .build();

        Set<ConstraintViolation<ProjectDTO>> violations = validator.validate(p);

        assertThat(violations).hasSize(0);
    }
}
