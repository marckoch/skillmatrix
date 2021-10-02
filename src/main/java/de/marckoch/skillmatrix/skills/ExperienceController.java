package de.marckoch.skillmatrix.skills;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
class ExperienceController {

    private final ExperienceRepository experienceRepository;

    private final DeveloperRepository developerRepository;

    private final SkillRepository skillRepository;

    public ExperienceController(ExperienceRepository experienceRepository, DeveloperRepository developerRepository,
                                SkillRepository skillRepository) {
        this.experienceRepository = experienceRepository;
        this.developerRepository = developerRepository;
        this.skillRepository = skillRepository;
    }

    public Map<Integer, String> getFreeSkills(Developer dev) {
        var skillsOfDeveloper = dev.getExperiences()
                .stream()
                .map(exp -> exp.getSkill().getSkillId())
                .toList();

        return skillRepository.findAll().stream()
                .filter(skill -> !skillsOfDeveloper.contains(skill.getSkillId()))
                .collect(Collectors.toMap(
                        Skill::getSkillId,
                        skill -> skill.getName() + " " + skill.getVersion()
                ));
    }

    @GetMapping("/experience/{developerId}/new")
    public String initCreationForm(Map<String, Object> model, @PathVariable("developerId") int developerId) {
        Developer dev = developerRepository.findById(developerId).get();
        model.put("developer", dev);

        Experience experience = new Experience();
        experience.setDeveloper(dev);
        model.put("experience", experience);

        model.put("skillSelectItems", getFreeSkills(dev));

        return "/experience/createOrUpdateExperienceForm";
    }

    @PostMapping("/experience/{developerId}/new")
    public String processCreationForm(Map<String, Object> model, @PathVariable("developerId") int developerId,
                                      @Valid Experience experience, BindingResult result) {
        Developer dev = developerRepository.findById(developerId).get();
        model.put("developer", dev);

        if (result.hasErrors()) {
            return "/experience/createOrUpdateExperienceForm";
        } else {
            experience.setDeveloper(dev);
            experienceRepository.save(experience);
            return "redirect:/developers/" + experience.getDeveloper().getDeveloperId();
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
