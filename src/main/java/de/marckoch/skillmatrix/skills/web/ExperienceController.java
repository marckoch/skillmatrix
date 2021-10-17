package de.marckoch.skillmatrix.skills.web;

import de.marckoch.skillmatrix.skills.entity.Developer;
import de.marckoch.skillmatrix.skills.entity.DeveloperRepository;
import de.marckoch.skillmatrix.skills.entity.Experience;
import de.marckoch.skillmatrix.skills.entity.ExperienceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Map;

import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.DEVELOPER;
import static de.marckoch.skillmatrix.skills.web.ModelAttributeNames.EXPERIENCE_DTO;

@Controller
@AllArgsConstructor
class ExperienceController {

    private final ExperienceRepository experienceRepository;

    private final DeveloperRepository developerRepository;

    @PostMapping("/experience/{developerId}/new")
    public String processAddSkillToDeveloperForm(@PathVariable("developerId") int developerId,
                                                 @Valid ExperienceDTO experienceDTO, BindingResult result,
                                                 final RedirectAttributes redirectAttributes, Map<String, Object> model) {
        Developer dev = developerRepository.findById(developerId).orElseThrow();
        model.put(DEVELOPER.modelAttributeName, dev);

        if (result.hasErrors()) {
            // save (erroneous model) for redirect
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.experienceDTO", result);
            redirectAttributes.addFlashAttribute(EXPERIENCE_DTO.modelAttributeName, experienceDTO);
        } else {
            Experience newExp = Experience.builder()
                    .skill(experienceDTO.getSkill())
                    .developer(dev)
                    .rating(experienceDTO.getRating())
                    .years(experienceDTO.getYears())
                    .build();

            experienceRepository.save(newExp);
        }

        return "redirect:/developers/" + dev.getDeveloperId();
    }

    @GetMapping("/experience/delete/{experienceId}")
    public String delete(@PathVariable("experienceId") int experienceId) {
        Developer dev = experienceRepository.findById(experienceId).orElseThrow().getDeveloper();
        dev.getExperiences().removeIf(e -> e.getExperienceId().equals(experienceId));
        developerRepository.save(dev);

        return "redirect:/developers/" + dev.getDeveloperId();
    }
}
