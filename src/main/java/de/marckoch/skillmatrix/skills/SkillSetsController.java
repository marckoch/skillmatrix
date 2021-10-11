package de.marckoch.skillmatrix.skills;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
class SkillSetsController {

    private final SkillRepository skillRepository;

    private final DeveloperRepository developerRepository;

    @GetMapping("/skills/sets")
    public String skillSets(@RequestParam(required = false) String skillSetQuery, Model model) {
        final List<Skill> skills = sortSkills(getSkills(skillSetQuery));

        final Set<Integer> developerIds = getDeveloperIds(skills);

        // TODO: search by JPA and boot gives an error! (Hansens column is empty!)
        // he and pehler both have weight of 40? maybe that is the reason?

        skills.forEach(skill -> {
            addEmptyExperienceForMissingDevelopers(skill, developerIds);

            // sort all experiences again by developer weight of selected skills descending
            skill.getExperiences().sort(Comparator.comparing(o -> -o.getDeveloper().getWeightForSkills(skills)));
        });

        model.addAttribute("skills", skills);
        model.addAttribute("skillSetQuery", buildJsonOfSkillSetQuery(skillSetQuery));
        return "/skills/skillSets";
    }

    // return search terms so token input can show them again (via its prePopulate mechanism)
    private List<Map<String, String>> buildJsonOfSkillSetQuery(String skillSetQuery) {
        if (skillSetQuery==null || skillSetQuery.isEmpty()) return Collections.emptyList();
        return Arrays.stream(skillSetQuery.split(",")).map(this::toMap).toList();
    }

    private Map<String, String> toMap(String s) {
        return Map.of("id", s, "name", s);
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
        if (query == null)
            return skillRepository.findAll();

        // this should/could be done in one query, but for now this is good enough.
        // user will enter only a handful of search terms, so we do a query for each of them.
        if (query.contains(",")) {
            List<String> searchTerms = Arrays.stream(query.split(",")).map(s -> s.trim().toUpperCase()).toList();
            return searchTerms.stream()
                    .map(skillRepository::findByQuery)
                    .flatMap(Collection::stream)
                    .distinct()
                    .collect(Collectors.toList());
        } else
            return skillRepository.findByQuery(query.toUpperCase());
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
