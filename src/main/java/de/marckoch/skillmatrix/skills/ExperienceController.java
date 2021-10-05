package de.marckoch.skillmatrix.skills;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Map;

@Controller
@AllArgsConstructor
class ExperienceController {

    private final ExperienceRepository experienceRepository;

    private final DeveloperRepository developerRepository;

    @PostMapping("/experience/{developerId}/new")
    public String processCreationForm(Map<String, Object> model, @PathVariable("developerId") int developerId,
                                      @Valid Experience experience, BindingResult result,
                                      final RedirectAttributes redirectAttributes) {
        Developer dev = developerRepository.findById(developerId).get();
        model.put("developer", dev);
        experience.setDeveloper(dev);

        if (result.hasErrors()) {
            // save (errornous model) for redirect
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.experience", result);
            redirectAttributes.addFlashAttribute("experience", experience);
        } else {
            experienceRepository.save(experience);
        }

        return "redirect:/developers/" + dev.getDeveloperId();
    }

    @GetMapping("/experience/delete/{experienceId}")
    public String delete(Map<String, Object> model, @PathVariable("experienceId") int experienceId) {
        Developer dev = experienceRepository.findById(experienceId).get().getDeveloper();
        dev.getExperiences().removeIf(e -> e.getExperienceid().equals(experienceId));
        developerRepository.save(dev);

        return "redirect:/developers/" + dev.getDeveloperId();
    }
}
