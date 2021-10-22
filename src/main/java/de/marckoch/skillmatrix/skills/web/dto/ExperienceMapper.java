package de.marckoch.skillmatrix.skills.web.dto;

import de.marckoch.skillmatrix.skills.entity.Experience;
import org.springframework.stereotype.Component;

@Component
public class ExperienceMapper {
    public Experience createNewEntityFromDTO(ExperienceDTO experienceDTO) {
        return Experience.builder()
                .skill(experienceDTO.getSkill())
                .developer(experienceDTO.getDeveloper())
                .rating(experienceDTO.getRating())
                .years(experienceDTO.getYears())
                .build();
    }
}
