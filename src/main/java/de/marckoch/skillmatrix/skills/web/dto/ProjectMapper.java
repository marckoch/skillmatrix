package de.marckoch.skillmatrix.skills.web.dto;

import de.marckoch.skillmatrix.skills.entity.Project;
import org.springframework.stereotype.Component;

import java.time.YearMonth;

@Component
public class ProjectMapper {

    public ProjectDTO buildProjectDTO(Project project) {
        return ProjectDTO.builder()
                .projectId(project.getProjectId())
                .name(project.getName())
                .since(project.getSince().toString())
                .until(project.getUntil().toString())
                .build();
    }

    public void updateEntityFromDTO(ProjectDTO dto, Project entity) {
        entity.setName(dto.getName());
        entity.setSince(YearMonth.parse(dto.getSince()));
        entity.setUntil(YearMonth.parse(dto.getUntil()));
    }
}
