package de.marckoch.skillmatrix.skills;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Map;

@Controller
@AllArgsConstructor
class ExperienceController {

    private final ExperienceRepository experienceRepository;

    private final DeveloperRepository developerRepository;

    @PostMapping("/experience/{developerId}/new")
    public String processCreationForm(Map<String, Object> model, @PathVariable("developerId") int developerId,
                                      @Valid Experience experience, BindingResult result) {
        Developer dev = developerRepository.findById(developerId).get();
        model.put("developer", dev);
        experience.setDeveloper(dev);

        if (result.hasErrors()) {
            return "redirect:/developers/" + dev.getDeveloperId();
        } else {
            experienceRepository.save(experience);
            return "redirect:/developers/" + dev.getDeveloperId();
        }
    }

    @GetMapping("/experience/delete/{experienceId}")
    public String delete(Map<String, Object> model, @PathVariable("experienceId") int experienceId) {
        Developer dev = experienceRepository.findById(experienceId).get().getDeveloper();
        dev.getExperiences().removeIf(e -> e.getExperienceid().equals(experienceId));
        developerRepository.save(dev);

        return "redirect:/developers/" + dev.getDeveloperId();
    }
}
