package de.marckoch.skillmatrix.skills;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
class SkillMatrixController {

    private final SkillRepository skillRepository;

    private final DeveloperRepository developerRepository;

    @GetMapping("/skills/matrix")
    public String skillMatrix(Model model) {
        List<Skill> allSkillsRanked = getAllSkillsRanked();

        List<Developer> allDevsRanked = getAllDevsRanked();

        List<Integer> idsOfAllDevelopers = allDevsRanked.stream().map(Developer::getDeveloperId).toList();

        allSkillsRanked.forEach(skill -> {
            List<Experience> experiences = skill.getExperiences();
            List<Integer> idsOfMissingDevs = findIdsOfMissingDevs(idsOfAllDevelopers, experiences);

            idsOfMissingDevs.forEach(devId -> {
                Experience e = createEmptyExperienceForDeveloper(devId);
                experiences.add(e);
            });

            // sort all experiences again by developer weight
            skill.getExperiences().sort(Comparator.comparing(o -> -o.getDeveloper().getWeight()));
        });

        model.addAttribute("skills", allSkillsRanked);
        return "/skills/matrix";
    }

    private List<Integer> findIdsOfMissingDevs(List<Integer> idsOfAllDevelopers, List<Experience> experiences) {
        final List<Integer> idsOfDevelopersWithThisSkill = experiences.stream()
                .map(exp -> exp.getDeveloper().getDeveloperId())
                .toList();
        return idsOfAllDevelopers.stream()
                .filter(id -> !idsOfDevelopersWithThisSkill.contains(id))
                .toList();
    }

    private Experience createEmptyExperienceForDeveloper(Integer id) {
        final Experience e = new Experience();
        e.setDeveloper(developerRepository.findById(id).get());
        e.setRating(0);
        e.setYears(0);
        return e;
    }

    private List<Developer> getAllDevsRanked() {
        Comparator<Developer> devWeightComp = Comparator.comparing(HasExperiences::getWeight);
        Comparator<Developer> devNameComp = Comparator.comparing(Developer::getLastName);

        return developerRepository.findAll()
                .stream()
                .sorted(devWeightComp.reversed().thenComparing(devNameComp))
                .collect(Collectors.toList());
    }

    private List<Skill> getAllSkillsRanked() {
        Comparator<Skill> skillWeightComp = Comparator.comparing(HasExperiences::getWeight);
        Comparator<Skill> skillNameComp = Comparator.comparing(Skill::getName);

        return skillRepository.findAll()
                .stream()
                .sorted(skillWeightComp.reversed().thenComparing(skillNameComp))
                .collect(Collectors.toList());
    }
}
