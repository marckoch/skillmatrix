package de.marckoch.skillmatrix.skills.web;

import de.marckoch.skillmatrix.skills.entity.Developer;
import de.marckoch.skillmatrix.skills.entity.Skill;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * Simple JavaBean domain object representing an experience, that is a connection between a person and a skill.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExperienceDTO {

    private Integer experienceId;

    private Developer developer;

    private Skill skill;

    @NotNull
    @Positive
    @Max(50)
    private Integer years;

    @NotNull
    @Range(min = 1, max = 5)
    private Integer rating;
}
