package de.marckoch.skillmatrix.skills;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
class SkillSetsController {

    private final SkillRepository skillRepository;

    private final DeveloperRepository developerRepository;

    @GetMapping("/skills/sets")
    public String skillSets(@RequestParam(required = false) String query, Model model) {
        final List<Skill> skills = sortSkills(getSkills(query));

        final Set<Integer> developerIds = getDeveloperIds(skills);

        skills.forEach(skill -> {
            addEmptyExperienceForMissingDevelopers(skill, developerIds);

            // sort all experiences again by developer weight of selected skills descending
            skill.getExperiences().sort(Comparator.comparing(o -> -o.getDeveloper().getWeightForSkills(skills)));
        });

        model.addAttribute("skills", skills);
        model.addAttribute("query", query);
        return "/skills/skillSets";
    }

    private void addEmptyExperienceForMissingDevelopers(Skill skill, Set<Integer> devIdsOfSkills) {
        List<Experience> experiences = skill.getExperiences();
        List<Integer> idsOfMissingDevs = findIdsOfMissingDevs(devIdsOfSkills, experiences);

        idsOfMissingDevs.forEach(devId -> {
            Experience e = createEmptyExperienceForDeveloper(devId);
            experiences.add(e);
        });
    }

    private List<Integer> findIdsOfMissingDevs(Set<Integer> idsOfDevelopers, List<Experience> experiences) {
        final List<Integer> idsOfDevelopersWithThisSkill = experiences.stream()
                .map(exp -> exp.getDeveloper().getDeveloperId())
                .toList();
        return idsOfDevelopers.stream()
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

    private List<Skill> getSkills(String query) {
        return query == null ?
                skillRepository.findAll() :
                skillRepository.findByQuery(query.toUpperCase());
    }

    private List<Skill> sortSkills(List<Skill> skills) {
        Comparator<Skill> skillWeightComp = Comparator.comparing(HasExperiences::getWeight);
        Comparator<Skill> skillNameComp = Comparator.comparing(Skill::getName);

        return skills.stream()
                .sorted(skillWeightComp.reversed().thenComparing(skillNameComp))
                .toList();
    }

    public Set<Integer> getDeveloperIds(List<Skill> skills) {
        return skills.stream()
                .flatMap(s -> s.getExperiences().stream()
                        .map(exp -> exp.getDeveloper().getDeveloperId()))
                .collect(Collectors.toSet());
    }
}
