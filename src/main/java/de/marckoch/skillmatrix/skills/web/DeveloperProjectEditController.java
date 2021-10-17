package de.marckoch.skillmatrix.skills.web;

import de.marckoch.skillmatrix.skills.entity.Developer;
import de.marckoch.skillmatrix.skills.entity.DeveloperRepository;
import de.marckoch.skillmatrix.skills.entity.Project;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.time.YearMonth;

import static de.marckoch.skillmatrix.skills.web.DeveloperEditController.REDIRECT_DEVELOPERS;
import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.DEVELOPER;
import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.PROJECT_DTO;

// maybe use this: https://qawithexperts.com/article/bootstrap/showing-month-and-year-only-in-bootstrap-datepicker/292
@Controller
@AllArgsConstructor
class DeveloperProjectEditController {

    public static final String CREATE_OR_UPDATE_PROJECT_VIEW = "/developers/createOrUpdateProjectForm";

    private final DeveloperRepository developerRepository;

    @GetMapping("/developers/{developerId}/project/add")
    public String initCreationForm(@PathVariable("developerId") int developerId, Model model) {
        Developer developer = developerRepository.findById(developerId).orElseThrow();
        model.addAttribute(DEVELOPER.modelAttributeName(), developer);

        ProjectDTO projectDTO = new ProjectDTO();
        model.addAttribute(PROJECT_DTO.modelAttributeName(), projectDTO);
        return CREATE_OR_UPDATE_PROJECT_VIEW;
    }

    @PostMapping("/developers/{developerId}/project/add")
    public String processCreationForm(@Valid ProjectDTO projectDTO, BindingResult result,
                                      @PathVariable("developerId") int developerId, Model model) {
        if (result.hasErrors()) {
            Developer developer = developerRepository.findById(developerId).orElseThrow();
            model.addAttribute(DEVELOPER.modelAttributeName(), developer);

            return CREATE_OR_UPDATE_PROJECT_VIEW;
        } else {
            Project newProject = new Project();
            updateEntityFromDTO(projectDTO, newProject);
            Developer developer = developerRepository.findById(developerId).orElseThrow();
            developer.setCurrentProject(newProject);
            Developer savedDev = developerRepository.save(developer);
            return REDIRECT_DEVELOPERS + savedDev.getDeveloperId();
        }
    }

    @GetMapping("/developers/{developerId}/project/edit")
    public String initUpdateProjectForm(@PathVariable("developerId") int developerId, Model model) {
        Developer developer = developerRepository.findById(developerId).orElseThrow();
        model.addAttribute(DEVELOPER.modelAttributeName(), developer);

        ProjectDTO dto = buildProjectDTO(developer.getCurrentProject());
        model.addAttribute(PROJECT_DTO.modelAttributeName(), dto);
        return CREATE_OR_UPDATE_PROJECT_VIEW;
    }

    @PostMapping("/developers/{developerId}/project/edit")
    public String processUpdateProjectForm(@Valid ProjectDTO developerDTO, BindingResult result,
                                           @PathVariable("developerId") int developerId, Model model) {
        if (result.hasErrors()) {
            Developer developer = developerRepository.findById(developerId).orElseThrow();
            model.addAttribute(DEVELOPER.modelAttributeName(), developer);

            return CREATE_OR_UPDATE_PROJECT_VIEW;
        } else {
            Developer existingDev = developerRepository.findById(developerId).orElseThrow();

            updateEntityFromDTO(developerDTO, existingDev.getCurrentProject());

            Developer savedDev = developerRepository.save(existingDev);
            model.addAttribute(DEVELOPER.modelAttributeName(), savedDev);
            return REDIRECT_DEVELOPERS + savedDev.getDeveloperId();
        }
    }

    @GetMapping("/developers/{developerId}/project/delete")
    public String deleteProject(@PathVariable("developerId") int developerId, Model model) {
        Developer existingDev = developerRepository.findById(developerId).orElseThrow();
        existingDev.setCurrentProject(null);
        Developer savedDev = developerRepository.save(existingDev);
        model.addAttribute(DEVELOPER.modelAttributeName(), savedDev);
        return REDIRECT_DEVELOPERS + savedDev.getDeveloperId();
    }

    private ProjectDTO buildProjectDTO(Project project) {
        return ProjectDTO.builder()
                .projectId(project.getProjectId())
                .name(project.getName())
                .since(project.getSince().toString())
                .until(project.getUntil().toString())
                .build();
    }

    private void updateEntityFromDTO(ProjectDTO dto, Project entity) {
        entity.setName(dto.getName());
        entity.setSince(YearMonth.parse(dto.getSince()));
        entity.setUntil(YearMonth.parse(dto.getUntil()));
    }
}
