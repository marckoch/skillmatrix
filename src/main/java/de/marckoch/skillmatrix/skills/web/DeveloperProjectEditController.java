package de.marckoch.skillmatrix.skills.web;

import de.marckoch.skillmatrix.skills.entity.Developer;
import de.marckoch.skillmatrix.skills.entity.DeveloperRepository;
import de.marckoch.skillmatrix.skills.entity.Project;
import de.marckoch.skillmatrix.skills.entity.ProjectRepository;
import de.marckoch.skillmatrix.skills.web.dto.ProjectDTO;
import de.marckoch.skillmatrix.skills.web.dto.ProjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.DEVELOPER;
import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.PROJECT_DTO;
import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.WAS_VALIDATED;
import static de.marckoch.skillmatrix.skills.web.ViewNames.CREATE_OR_UPDATE_PROJECT_VIEW;
import static de.marckoch.skillmatrix.skills.web.ViewNames.REDIRECT_DEVELOPERS;

// maybe use this: https://qawithexperts.com/article/bootstrap/showing-month-and-year-only-in-bootstrap-datepicker/292
@Controller
@AllArgsConstructor
class DeveloperProjectEditController {

    private final DeveloperRepository developerRepository;

    private final ProjectRepository projectRepository;

    private final ProjectMapper projectMapper;

    @GetMapping("/developers/{developerId}/project/add")
    public String initCreationForm(@PathVariable("developerId") int developerId, Model model) {
        Developer developer = developerRepository.findById(developerId).orElseThrow();
        model.addAttribute(DEVELOPER, developer);

        ProjectDTO projectDTO = new ProjectDTO();
        model.addAttribute(PROJECT_DTO, projectDTO);
        return CREATE_OR_UPDATE_PROJECT_VIEW;
    }

    @PostMapping("/developers/{developerId}/project/add")
    public String processCreationForm(@Valid ProjectDTO projectDTO, BindingResult result,
                                      @PathVariable("developerId") int developerId, Model model) {
        model.addAttribute(WAS_VALIDATED, true);
        if (result.hasErrors()) {
            Developer developer = developerRepository.findById(developerId).orElseThrow();
            model.addAttribute(DEVELOPER, developer);

            return CREATE_OR_UPDATE_PROJECT_VIEW;
        } else {
            Project newProject = new Project();
            projectMapper.updateEntityFromDTO(projectDTO, newProject);
            Developer developer = developerRepository.findById(developerId).orElseThrow();
            developer.setCurrentProject(newProject);
            Developer savedDev = developerRepository.save(developer);
            return REDIRECT_DEVELOPERS + '/' + savedDev.getDeveloperId();
        }
    }

    @PostMapping(value = "/developers/{developerId}/project/add", params = "cancel")
    public String processCreationFormCancel(@PathVariable("developerId") int developerId) {
        return REDIRECT_DEVELOPERS + '/' +  developerId;
    }

    @GetMapping("/developers/{developerId}/project/edit")
    public String initUpdateProjectForm(@PathVariable("developerId") int developerId, Model model) {
        Developer developer = developerRepository.findById(developerId).orElseThrow();
        model.addAttribute(DEVELOPER, developer);

        ProjectDTO dto = projectMapper.buildProjectDTO(developer.getCurrentProject());
        model.addAttribute(PROJECT_DTO, dto);
        return CREATE_OR_UPDATE_PROJECT_VIEW;
    }

    @PostMapping("/developers/{developerId}/project/edit")
    public String processUpdateProjectForm(@Valid ProjectDTO projectDTO, BindingResult result,
                                           @PathVariable("developerId") int developerId, Model model) {
        model.addAttribute(WAS_VALIDATED, true);
        if (result.hasErrors()) {
            Developer developer = developerRepository.findById(developerId).orElseThrow();
            model.addAttribute(DEVELOPER, developer);

            return CREATE_OR_UPDATE_PROJECT_VIEW;
        } else {
            Developer existingDev = developerRepository.findById(developerId).orElseThrow();

            projectMapper.updateEntityFromDTO(projectDTO, existingDev.getCurrentProject());

            Developer savedDev = developerRepository.save(existingDev);
            model.addAttribute(DEVELOPER, savedDev);
            return REDIRECT_DEVELOPERS + '/' + savedDev.getDeveloperId();
        }
    }

    @PostMapping(value = "/developers/{developerId}/project/edit", params = "cancel")
    public String processUpdateFormCancel(@PathVariable("developerId") int developerId) {
        return REDIRECT_DEVELOPERS + '/' +  developerId;
    }

    @Transactional
    @GetMapping("/developers/{developerId}/project/delete")
    public String deleteProject(@PathVariable("developerId") int developerId, Model model) {
        Developer existingDev = developerRepository.findById(developerId).orElseThrow();
        projectRepository.deleteById(existingDev.getCurrentProject().getProjectId());
        existingDev.setCurrentProject(null);
        Developer savedDev = developerRepository.save(existingDev);
        model.addAttribute(DEVELOPER, savedDev);
        return REDIRECT_DEVELOPERS + '/' + savedDev.getDeveloperId();
    }
}