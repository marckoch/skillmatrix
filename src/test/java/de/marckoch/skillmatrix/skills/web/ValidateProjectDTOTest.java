package de.marckoch.skillmatrix.skills.web;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ValidateProjectDTOTest {

    protected Validator validator;

    @BeforeEach
    public void setUp() {
        validator = Validation
                .buildDefaultValidatorFactory()
                .getValidator();
    }

    @Test
    void sinceBeforeUntilViolationIsDetected() {
        ProjectDTO p = ProjectDTO.builder()
                .name("test")
                .since("2020-03")
                .until("2014-04")
                .build();

        Set<ConstraintViolation<ProjectDTO>> violations = validator.validate(p);

        assertThat(violations).hasSize(1);
        violations.forEach(v -> assertThat(v.getMessage()).contains("'since' must be before 'until'"));
    }

    @Test
    void noViolationWhenSinceIsBeforeUntil() {
        ProjectDTO p = ProjectDTO.builder()
                .name("test")
                .since("2020-03")
                .until("2021-04")
                .build();

        Set<ConstraintViolation<ProjectDTO>> violations = validator.validate(p);

        assertThat(violations).isEmpty();
    }

    @Test
    void sinceIsMissingViolationDetected() {
        ProjectDTO p = ProjectDTO.builder()
                .name("test")
                .until("2021-04")
                .build();

        Set<ConstraintViolation<ProjectDTO>> violations = validator.validate(p);

        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting(v -> new Tuple(v.getPropertyPath().toString(), v.getMessage()))
                .containsExactly(new Tuple("since", "must not be empty"));
    }

    @Test
    void emptySinceViolationDetected() {
        ProjectDTO p = ProjectDTO.builder()
                .name("test")
                .since("")
                .until("2021-04")
                .build();

        Set<ConstraintViolation<ProjectDTO>> violations = validator.validate(p);

        assertThat(violations).hasSize(2);
        assertThat(violations)
                .extracting(v -> new Tuple(v.getPropertyPath().toString(), v.getMessage()))
                .containsExactlyInAnyOrder(
                        new Tuple("since", "must not be empty"),
                        new Tuple("since", "must match yyyy-MM, e.g. 2006-11"));
    }

    @Test
    void wrongFormatForSinceViolationDetected() {
        ProjectDTO p = ProjectDTO.builder()
                .name("test")
                .since("xxx")
                .until("2021-04")
                .build();

        Set<ConstraintViolation<ProjectDTO>> violations = validator.validate(p);

        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting(v -> new Tuple(v.getPropertyPath().toString(), v.getMessage()))
                .containsExactly(new Tuple("since", "must match yyyy-MM, e.g. 2006-11"));
    }
}
