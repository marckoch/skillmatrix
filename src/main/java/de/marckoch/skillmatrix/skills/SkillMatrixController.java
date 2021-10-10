package de.marckoch.skillmatrix.skills;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
class SkillMatrixController {

    private final SkillRepository skillRepository;

    private final DeveloperRepository developerRepository;

    @GetMapping("/skills/matrix")
    public String skillMatrix(Model model) {
        final List<Skill> skills = getAllSkillsRanked();

        final Set<Integer> developerIds = getDeveloperIds();

        skills.forEach(skill -> {
            addEmptyExperienceForMissingDevelopers(skill, developerIds);

            // sort all experiences again by developer weight descending
            skill.getExperiences().sort(Comparator.comparing(o -> -o.getDeveloper().getWeight()));
        });

        model.addAttribute("skills", skills);
        return "/skills/skillMatrix";
    }

    private void addEmptyExperienceForMissingDevelopers(Skill skill, Set<Integer> devIdsOfSkills) {
        List<Experience> experiences = skill.getExperiences();
        List<Integer> idsOfMissingDevs = findIdsOfMissingDevs(devIdsOfSkills, experiences);

        idsOfMissingDevs.forEach(devId -> {
            Experience e = createEmptyExperienceForDeveloper(devId);
            experiences.add(e);
        });
    }

    private List<Integer> findIdsOfMissingDevs(Set<Integer> idsOfAllDevelopers, List<Experience> experiences) {
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

    private List<Skill> getAllSkillsRanked() {
        Comparator<Skill> skillWeightComp = Comparator.comparing(HasExperiences::getWeight);
        Comparator<Skill> skillNameComp = Comparator.comparing(Skill::getName);

        return skillRepository.findAll()
                .stream()
                .sorted(skillWeightComp.reversed().thenComparing(skillNameComp))
                .collect(Collectors.toList());
    }

    public Set<Integer> getDeveloperIds() {
        return developerRepository.findAll()
                .stream()
                .map(Developer::getDeveloperId)
                .collect(Collectors.toSet());
    }
}