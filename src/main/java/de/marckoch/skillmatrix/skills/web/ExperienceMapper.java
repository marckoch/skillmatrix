package de.marckoch.skillmatrix.skills.web;

import de.marckoch.skillmatrix.skills.entity.Experience;
import org.springframework.stereotype.Component;

@Component
public class ExperienceMapper {
    public ExperienceDTO buildDTO(Experience experience) {
        return ExperienceDTO.builder()
                .developer(experience.getDeveloper())
                .experienceId(experience.getExperienceId())
                .rating(experience.getRating())
                .years(experience.getYears())
                .build();
    }

    public Experience createNewEntityFromDTO(ExperienceDTO experienceDTO) {
        return Experience.builder()
                .skill(experienceDTO.getSkill())
                .developer(experienceDTO.getDeveloper())
                .rating(experienceDTO.getRating())
                .years(experienceDTO.getYears())
                .build();
    }
}
