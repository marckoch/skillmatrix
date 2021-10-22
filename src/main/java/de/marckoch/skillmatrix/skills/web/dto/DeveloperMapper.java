package de.marckoch.skillmatrix.skills.web.dto;

import de.marckoch.skillmatrix.skills.entity.Developer;
import org.springframework.stereotype.Component;

@Component
public class DeveloperMapper {

    public DeveloperDTO buildDeveloperDTO(Developer developer) {
        return DeveloperDTO.builder()
                .developerId(developer.getDeveloperId())
                .firstName(developer.getFirstName())
                .lastName(developer.getLastName())
                .title(developer.getTitle())
                .build();
    }

    public void updateEntityFromDTO(DeveloperDTO dto, Developer entity) {
        entity.setLastName(dto.getLastName());
        entity.setFirstName(dto.getFirstName());
        entity.setTitle(dto.getTitle());
    }
}
